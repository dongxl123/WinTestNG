package com.winbaoxian.testng;

import com.winbaoxian.testng.core.WinTestNGExecutor;
import com.winbaoxian.testng.enums.TriggerMode;
import com.winbaoxian.testng.model.core.log.TestReportDataSummaryDTO;
import com.winbaoxian.testng.model.core.log.TestReportDataTestCaseDTO;
import com.winbaoxian.testng.model.dto.TestCasesDTO;
import com.winbaoxian.testng.utils.UUIDUtil;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;
import org.testng.xml.XmlTest;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.Date;


@SpringBootTest
public class WinTestNGTests extends AbstractTestNGSpringContextTests {

    @Resource
    private WinTestNGExecutor winTestNGExecutor;
    private String sql;
    private TestReportDataSummaryDTO summaryData;

    @Parameters({"sql"})
    @BeforeClass
    public void beforeClass(String sql) {
        String projectIdStr = System.getProperty("ProjectId");
        long projectId = NumberUtils.toLong(projectIdStr);
        summaryData = new TestReportDataSummaryDTO();
        if (projectId > 0) {
            this.sql = String.format("SELECT a.* FROM test_cases a, project b WHERE a.project_id = b.id AND a.ci_flag = TRUE AND a.deleted = FALSE AND b.deleted = FALSE AND b.integration_flag = TRUE AND a.project_id = %s", projectId);
            summaryData.setTriggerMode(TriggerMode.AUTO);
            summaryData.setProjectId(projectId);
            summaryData.setAnalysisFlag(Boolean.TRUE);
        } else {
            this.sql = sql;
            summaryData.setTriggerMode(TriggerMode.SCRIPT);
        }
        summaryData.setReportUuid(UUIDUtil.INSTANCE.randomUUID());
        summaryData.setStartTime(new Date());
    }

    /**
     * 查询测试用例
     * parallel = true
     *
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @DataProvider(name = "testData")
    public Object[] getData() throws SQLException, ClassNotFoundException {
        //判断是否跳过自动化测试
//        if (TriggerMode.AUTO.equals(summaryData.getTriggerMode()) && winTestNGExecutor.needSkipAutomatedTesting(summaryData.getProjectId())) {
//            return new Object[0];
//        }
        if (TriggerMode.AUTO.equals(summaryData.getTriggerMode())) {
            return new Object[0];
        }
        TestCasesDTO[] dataArray = winTestNGExecutor.getTestCasesList(sql);
        summaryData.setTotalCount((long) dataArray.length);
        return dataArray;
    }

    @BeforeMethod
    public void beforeMethod() {
    }


    @Test(dataProvider = "testData")
    public void execute(TestCasesDTO testCasesDTO) throws Throwable {
        TestReportDataTestCaseDTO reportDataContext = new TestReportDataTestCaseDTO();
        reportDataContext.setReportUuid(summaryData.getReportUuid());
        reportDataContext.setAnalysisFlag(summaryData.getAnalysisFlag());
        summaryData.addTestCaseData(reportDataContext);
        try {
            winTestNGExecutor.executeTestCase(testCasesDTO, reportDataContext);
            summaryData.incSuccessCount();
        } catch (Throwable e) {
            summaryData.incFailCount();
            throw e;
        }
    }

    @AfterMethod
    public void afterMethod(ITestContext a, XmlTest b, Method c, Object[] d, ITestResult e) {
    }

    @AfterClass
    public void afterClass() {
        summaryData.setEndTime(new Date());
        //保存测试报告数据、清理缓存、发送邮件、分析项目质量分
        winTestNGExecutor.doWorkAfterFinished(summaryData);
    }


}
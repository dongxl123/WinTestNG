package com.winbaoxian.testng.model.core.log;

import com.winbaoxian.testng.enums.RunState;
import com.winbaoxian.testng.enums.TriggerMode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2019-03-26 16:10
 */
@Getter
@Setter
public class TestReportDataSummaryDTO extends TestReportDataBaseCountDTO {

    /**
     * 触发方式
     */
    private TriggerMode triggerMode;

    /**
     * 运行状态
     */
    private RunState runState;

    /**
     * 项目ID，有值时,按项目触发执行
     */
    private Long projectId;

    /**
     * 是否是分析模式
     */
    private Boolean analysisFlag;

    /**
     * 每个testCase的测试报告数据
     */
    private List<TestReportDataTestCaseDTO> testCaseDataList;

    public RunState getRunState() {
        return getFailCount() > 0 ? RunState.FAIL : RunState.SUCCESS;
    }

    public synchronized void addTestCaseData(TestReportDataTestCaseDTO testCaseData) {
        if (testCaseData == null) {
            return;
        }
        if (CollectionUtils.isEmpty(testCaseDataList)) {
            testCaseDataList = new ArrayList<>();
        }
        testCaseDataList.add(testCaseData);
    }

}

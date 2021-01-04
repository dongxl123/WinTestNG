package com.winbaoxian.testng.core.action.normal;

import com.ql.util.express.ExpressRunner;
import com.winbaoxian.common.freemarker.utils.JsonUtils;
import com.winbaoxian.testng.core.action.IAction;
import com.winbaoxian.testng.enums.AssertionType;
import com.winbaoxian.testng.model.core.TestCasesRunContext;
import com.winbaoxian.testng.model.core.action.normal.AssertVerifyConfigDTO;
import com.winbaoxian.testng.model.core.action.normal.AssertionActionSetting;
import com.winbaoxian.testng.model.core.log.TestReportDataTestCaseDTO;
import com.winbaoxian.testng.utils.ConsoleLogUtils;
import com.winbaoxian.testng.utils.WinTestNGLogUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Component;
import org.testng.Assert;

/**
 * @author dongxuanliang252
 * @date 2019-03-06 14:42
 */
@Component
@Slf4j
public class AssertionAction implements IAction<AssertionActionSetting> {

    @Override
    public Object execute(AssertionActionSetting actionSetting, TestCasesRunContext context) throws Exception {
        if (actionSetting == null) {
            return true;
        }
        TestReportDataTestCaseDTO reportDataContext = context.getReportDataContext();
        String[] testReportTitles = WinTestNGLogUtils.INSTANCE.getTestReportTitles(actionSetting, context);
        boolean assertSuccessFlag = true;
        for (AssertVerifyConfigDTO verifyConfigDTO : actionSetting.getVerifyList()) {
            String message = String.format("Assertion: %s", JsonUtils.INSTANCE.toJSONString(verifyConfigDTO));
            AssertionType assertType = verifyConfigDTO.getType();
            String value1 = verifyConfigDTO.getValue1();
            String value2 = verifyConfigDTO.getValue2();
            try {
                if (AssertionType.condition.equals(assertType)) {
                    String express = value1;
                    ExpressRunner runner = new ExpressRunner();
                    Object result = null;
                    try {
                        result = runner.execute(express, null, null, true, false);
                    } catch (Exception e) {
                        log.error("QLExpress execute error,express:{}", express, e);
                    }
                    Assert.assertTrue((Boolean) result, message);
                } else if (AssertionType.eq.equals(assertType)) {
                    Assert.assertEquals(value1, value2, message);
                } else if (AssertionType.ne.equals(assertType)) {
                    Assert.assertNotEquals(value1, value2, message);
                } else if (AssertionType.contains.equals(assertType)) {
                    Assert.assertTrue(value1.contains(value2), message);
                } else if (AssertionType.notcontains.equals(assertType)) {
                    Assert.assertFalse(value1.contains(value2), message);
                } else if (AssertionType.regex.equals(assertType)) {
                    String regex = value2;
                    Assert.assertTrue(value1.matches(regex), message);
                }
                log.info("Assertion: {}, 断言成功", JsonUtils.INSTANCE.toJSONString(verifyConfigDTO));
                if (BooleanUtils.isTrue(reportDataContext.getDebugFlag())) {
                    ConsoleLogUtils.INSTANCE.log(reportDataContext.getReportUuid(), String.format("Assertion: %s, 断言成功", JsonUtils.INSTANCE.toJSONString(verifyConfigDTO)));
                }
                reportDataContext.logText(String.format("断言内容:%s, 断言成功", JsonUtils.INSTANCE.toJSONString(verifyConfigDTO)), testReportTitles);
            } catch (AssertionError e) {
                log.error("Assertion: {}, 断言失败", JsonUtils.INSTANCE.toJSONString(verifyConfigDTO));
                if (BooleanUtils.isTrue(reportDataContext.getDebugFlag())) {
                    ConsoleLogUtils.INSTANCE.log(reportDataContext.getReportUuid(), String.format("Assertion: %s, 断言失败", JsonUtils.INSTANCE.toJSONString(verifyConfigDTO)));
                }
                reportDataContext.logText(String.format("断言内容:%s, 断言失败", JsonUtils.INSTANCE.toJSONString(verifyConfigDTO)), testReportTitles);
                reportDataContext.logAssertionError(e, testReportTitles);
                //断言失败，不继续执行
                throw e;
            }
        }
        return assertSuccessFlag;
    }

}

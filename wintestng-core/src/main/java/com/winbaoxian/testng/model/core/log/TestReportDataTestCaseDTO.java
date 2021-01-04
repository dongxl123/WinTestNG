package com.winbaoxian.testng.model.core.log;

import com.winbaoxian.testng.enums.RunState;
import com.winbaoxian.testng.utils.WinTestNGLogUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 存储测试任务报告数据、通过变量控制不同的执行行为
 *
 * @author dongxuanliang252
 * @date 2019-03-26 16:10
 */
@Getter
@Setter
public class TestReportDataTestCaseDTO extends TestReportDataBaseCountDTO {

    public final static int STATUS_SUCCESS = 0;
    public final static int STATUS_ASSERTION_ERROR = 1;
    public final static int STATUS_EXCEPTION = 2;

    /**
     * 测试用例ID
     */
    private Long testCasesId;
    /**
     * 记录testcase的多个异常(非断言失败)，该异常非配置问题
     */
    private transient List<Exception> exceptions;

    private transient Map<String, Object> tplLockCache = new HashMap<>();

    private List<String> errorMessages;

    private String currentTitle;
    /**
     * 步骤日志
     */
    private List<TestReportDataStepLogDTO> stepDataList;
    /**
     * 0: 成功， 1：断言失败，2：异常
     */
    private int status = STATUS_SUCCESS;

    /**
     * 运行状态
     */
    private RunState runState;

    /**
     * 是否是调式模式
     */
    private Boolean debugFlag;

    /**
     * 是否是分析模式
     */
    private Boolean analysisFlag;

    public RunState getRunState() {
        return status == STATUS_SUCCESS ? RunState.SUCCESS : RunState.FAIL;
    }

    public TestReportDataTestCaseDTO setCurrentTitle(String currentTitle) {
        this.currentTitle = currentTitle;
        return this;
    }

    public int getCurrentStepStatus() {
        if (StringUtils.isEmpty(this.currentTitle)) {
            return status;
        }
        for (TestReportDataStepLogDTO testReportDataStepDetailDTO : stepDataList) {
            if (StringUtils.equals(this.currentTitle, testReportDataStepDetailDTO.getTitle())) {
                return testReportDataStepDetailDTO.getStatus();
            }
        }
        return status;
    }

    /**
     * 记录提示信息
     */
    public TestReportDataTestCaseDTO logNoticeMsg(String noticeMsg, String... subTitles) {
        if (StringUtils.isBlank(noticeMsg) || StringUtils.isBlank(this.currentTitle)) {
            return this;
        }
        if (CollectionUtils.isEmpty(stepDataList)) {
            return this;
        }
        TestReportDataStepLogDTO stepDetailDTO = null;
        for (TestReportDataStepLogDTO testReportDataStepDetailDTO : stepDataList) {
            if (StringUtils.equals(this.currentTitle, testReportDataStepDetailDTO.getTitle())) {
                stepDetailDTO = testReportDataStepDetailDTO;
            }
        }
        if (stepDetailDTO == null) {
            return this;
        }
        if (ArrayUtils.isEmpty(subTitles)) {
            stepDetailDTO.setNoticeMsg(noticeMsg);
        } else {
            stepDetailDTO.logNoticeMsg(subTitles, noticeMsg);
        }
        return this;
    }

    /**
     * 记录日志
     */
    public TestReportDataTestCaseDTO logText(String text, String... subTitles) {
        return logText(text, STATUS_SUCCESS, subTitles);
    }

    public TestReportDataTestCaseDTO setStepLogEndFlag(String... subTitles) {
        if (StringUtils.isBlank(this.currentTitle) || CollectionUtils.isEmpty(this.getStepDataList())) {
            return this;
        }
        TestReportDataStepLogDTO stepDetailDTO = null;
        for (TestReportDataStepLogDTO testReportDataStepDetailDTO : stepDataList) {
            if (StringUtils.equals(this.currentTitle, testReportDataStepDetailDTO.getTitle()) && !testReportDataStepDetailDTO.isEndFlag()) {
                stepDetailDTO = testReportDataStepDetailDTO;
            }
        }
        if (stepDetailDTO == null) {
            return this;
        }
        stepDetailDTO.setStepLogEndFlag(subTitles);
        return this;
    }

    public TestReportDataTestCaseDTO logException(Exception e, String... subTitles) {
        if (e == null) {
            return this;
        }
        addException(e);
        return logText(WinTestNGLogUtils.INSTANCE.getErrorString(e), STATUS_EXCEPTION, subTitles);
    }

    public TestReportDataTestCaseDTO logAssertionError(AssertionError e, String... subTitles) {
        if (e == null) {
            return this;
        }
        return logText(WinTestNGLogUtils.INSTANCE.getErrorString(e), STATUS_ASSERTION_ERROR, subTitles);
    }

    private TestReportDataTestCaseDTO logText(String text, int status, String... subTitles) {
        if (StringUtils.isBlank(text) || StringUtils.isBlank(this.currentTitle)) {
            return this;
        }
        if (CollectionUtils.isEmpty(stepDataList)) {
            stepDataList = new ArrayList<>();
        }
        this.status = Math.max(this.status, status);
        TestReportDataStepLogDTO stepDetailDTO = null;
        for (TestReportDataStepLogDTO testReportDataStepDetailDTO : stepDataList) {
            if (StringUtils.equals(this.currentTitle, testReportDataStepDetailDTO.getTitle()) && !testReportDataStepDetailDTO.isEndFlag()) {
                stepDetailDTO = testReportDataStepDetailDTO;
            }
        }
        if (stepDetailDTO == null) {
            stepDetailDTO = new TestReportDataStepLogDTO();
            stepDetailDTO.setTitle(this.currentTitle);
            stepDataList.add(stepDetailDTO);
        }
        stepDetailDTO.logText(subTitles, status, text);
        return this;
    }

    private void addException(Exception e) {
        if (CollectionUtils.isEmpty(exceptions)) {
            exceptions = new ArrayList<>();
        }
        exceptions.add(e);
    }

    public List<String> getErrorMessages() {
        if (CollectionUtils.isNotEmpty(errorMessages)) {
            return errorMessages;
        }
        if (CollectionUtils.isEmpty(exceptions)) {
            return null;
        }
        List<String> strList = new ArrayList<>();
        for (Throwable e : exceptions) {
            strList.add(WinTestNGLogUtils.INSTANCE.getErrorString(e));
        }
        return strList;
    }

}

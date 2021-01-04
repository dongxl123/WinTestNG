package com.winbaoxian.testng.model.core.log;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2019-03-26 16:10
 */
@Getter
@Setter
public class TestReportDataStepLogDTO implements TestReportDataBaseLogDTO {

    /**
     * 执行case步骤的title
     */
    private String title;

    /**
     * 提示信息
     */
    private String noticeMsg;

    /**
     * 详细日志
     */
    private List<Object> logDataList;
    /**
     * 0: 成功， 1：断言失败，2：异常
     */
    private int status = TestReportDataTestCaseDTO.STATUS_SUCCESS;

    private transient boolean endFlag = false;

    public void logText(String[] subTitles, int status, String text) {
        if (StringUtils.isBlank(text)) {
            return;
        }
        if (CollectionUtils.isEmpty(logDataList)) {
            logDataList = new ArrayList<>();
        }
        this.status = Math.max(this.status, status);
        if (ArrayUtils.isEmpty(subTitles)) {
            TestReportDataTextLogDTO logDTO = new TestReportDataTextLogDTO();
            logDTO.setTime(new Date());
            logDTO.setText(text);
            logDataList.add(logDTO);
        } else {
            TestReportDataStepLogDTO stepDetailDTO = null;
            for (Object logDTO : logDataList) {
                if (logDTO instanceof TestReportDataStepLogDTO && StringUtils.equals(((TestReportDataStepLogDTO) logDTO).getTitle(), subTitles[0]) && !((TestReportDataStepLogDTO) logDTO).isEndFlag()) {
                    stepDetailDTO = (TestReportDataStepLogDTO) logDTO;
                }
            }
            if (stepDetailDTO == null) {
                stepDetailDTO = new TestReportDataStepLogDTO();
                stepDetailDTO.setTitle(subTitles[0]);
                logDataList.add(stepDetailDTO);
            }
            stepDetailDTO.logText(ArrayUtils.subarray(subTitles, 1, subTitles.length), status, text);
        }
    }

    public void logNoticeMsg(String[] subTitles, String noticeMsg) {
        if (StringUtils.isBlank(noticeMsg)) {
            return;
        }
        if (CollectionUtils.isEmpty(logDataList)) {
            return;
        }
        if (ArrayUtils.isNotEmpty(subTitles)) {
            TestReportDataStepLogDTO stepDetailDTO = null;
            for (Object logDTO : logDataList) {
                if (logDTO instanceof TestReportDataStepLogDTO && StringUtils.equals(((TestReportDataStepLogDTO) logDTO).getTitle(), subTitles[0])) {
                    stepDetailDTO = (TestReportDataStepLogDTO) logDTO;
                }
            }
            if (stepDetailDTO == null) {
                return;
            }
            if (subTitles.length == 1) {
                if (StringUtils.isNotBlank(stepDetailDTO.getNoticeMsg())) {
                    stepDetailDTO.setNoticeMsg(String.format("%s,%s", stepDetailDTO.getNoticeMsg(), noticeMsg));
                } else {
                    stepDetailDTO.setNoticeMsg(noticeMsg);
                }
            } else {
                stepDetailDTO.logNoticeMsg(ArrayUtils.subarray(subTitles, 1, subTitles.length), noticeMsg);
            }
        }
    }

    public void setStepLogEndFlag(String[] subTitles) {
        if (ArrayUtils.isEmpty(subTitles)) {
            this.setEndFlag(true);
            return;
        } else if (CollectionUtils.isEmpty(logDataList)) {
            return;
        } else {
            TestReportDataStepLogDTO stepDetailDTO = null;
            for (Object logDTO : logDataList) {
                if (logDTO instanceof TestReportDataStepLogDTO && StringUtils.equals(((TestReportDataStepLogDTO) logDTO).getTitle(), subTitles[0]) && !((TestReportDataStepLogDTO) logDTO).isEndFlag()) {
                    stepDetailDTO = (TestReportDataStepLogDTO) logDTO;
                }
            }
            if (stepDetailDTO == null) {
                return;
            }
            stepDetailDTO.setStepLogEndFlag(ArrayUtils.subarray(subTitles, 1, subTitles.length));
        }
    }


}

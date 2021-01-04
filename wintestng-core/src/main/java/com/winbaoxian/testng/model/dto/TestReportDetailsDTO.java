package com.winbaoxian.testng.model.dto;/*
 * Welcome to use the TableGo Tools.
 *
 * http://vipbooks.iteye.com
 * http://blog.csdn.net/vipbooks
 * http://www.cnblogs.com/vipbooks
 *
 * Author:bianj
 * Email:edinsker@163.com
 * Version:5.0.0
 */

import com.winbaoxian.testng.model.core.log.TestReportDataStepLogDTO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * TEST_REPORT_DETAILS
 *
 * @author bianj
 * @version 1.0.0 2019-03-20
 */
@Getter
@Setter
public class TestReportDetailsDTO implements Serializable {

    private Long id;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 报告唯一UID
     */
    private String reportUuid;

    /**
     * 测试用例ID
     */
    private Long testCasesId;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 花费的时间(单位毫秒)
     */
    private Long duration;

    /**
     * 总数
     */
    private Long totalCount;

    /**
     * 成功数
     */
    private Long successCount;

    /**
     * 失败数
     */
    private Long failCount;

    /**
     * 运行状态
     */
    private Integer runState;

    /**
     * 详细日志
     */
    private List<TestReportDataStepLogDTO> details;

    /**
     * 异常日志
     */
    private List<String> exceptions;

    /**
     * 是否删除， 1:删除， 0:有效
     */
    private Boolean deleted;

}
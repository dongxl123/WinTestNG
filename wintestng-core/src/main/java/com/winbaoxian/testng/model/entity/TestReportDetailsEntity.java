package com.winbaoxian.testng.model.entity;/*
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

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * TEST_REPORT_DETAILS
 *
 * @author bianj
 * @version 1.0.0 2019-03-20
 */
@Entity
@Setter
@Getter
@DynamicInsert
@DynamicUpdate
@Table(name = "TEST_REPORT_DETAILS")
public class TestReportDetailsEntity implements Serializable {
    /**
     * 版本号
     */
    private static final long serialVersionUID = 504924006308267822L;

    /**
     *
     */
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    /**
     * 创建时间
     */
    @CreationTimestamp
    @Column(name = "CREATE_TIME")
    private Date createTime;

    /**
     * 更新时间
     */
    @UpdateTimestamp
    @Column(name = "UPDATE_TIME")
    private Date updateTime;

    /**
     * 报告唯一UID
     */
    @Column(name = "REPORT_UUID")
    private String reportUuid;

    /**
     * 测试用例ID
     */
    @Column(name = "TEST_CASES_ID")
    private Long testCasesId;

    /**
     * 开始时间
     */
    @Column(name = "START_TIME")
    private Date startTime;

    /**
     * 结束时间
     */
    @Column(name = "END_TIME")
    private Date endTime;

    /**
     * 花费的时间(单位毫秒)
     */
    @Column(name = "DURATION")
    private Long duration;

    /**
     * 总数
     */
    @Column(name = "TOTAL_COUNT")
    private Long totalCount;

    /**
     * 成功数
     */
    @Column(name = "SUCCESS_COUNT")
    private Long successCount;

    /**
     * 失败数
     */
    @Column(name = "FAIL_COUNT")
    private Long failCount;

    /**
     * 运行状态
     */
    @Column(name = "RUN_STATE")
    private Integer runState;

    /**
     * 详细日志
     */
    @Column(name = "DETAILS")
    private String details;

    /**
     * 异常日志
     */
    @Column(name = "EXCEPTIONS")
    private String exceptions;

    /**
     * 是否删除， 1:删除， 0:有效
     */
    @Column(name = "DELETED")
    private Boolean deleted;

}
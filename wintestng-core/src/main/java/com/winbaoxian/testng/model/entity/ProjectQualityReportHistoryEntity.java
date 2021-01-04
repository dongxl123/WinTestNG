package com.winbaoxian.testng.model.entity;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 项目质量报告(ProjectQualityReportHistory)实体类
 *
 * @author dongxuanliang252
 * @date 2020-06-24 16:52:19
 */
@Entity
@Setter
@Getter
@DynamicInsert
@DynamicUpdate
@Table(name = "PROJECT_QUALITY_REPORT_HISTORY")
public class ProjectQualityReportHistoryEntity implements Serializable {

    private static final long serialVersionUID = -72799326455671225L;
    
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
     * 日期范围
     */
    @Column(name = "DATE_RANGE")
    private String dateRange;
    /**
     * 项目ID
     */
    @Column(name = "PROJECT_ID")
    private Long projectId;
    /**
     * 测试负责人ID，多个用，隔开
     */
    @Column(name = "TEST_OWNER_IDS")
    private String testOwnerIds;
    /**
     * 开发负责人ID，多个用,隔开
     */
    @Column(name = "DEV_OWNER_IDS")
    private String devOwnerIds;
    /**
     * 自动执行次数
     */
    @Column(name = "RUN_TOTAL_COUNT")
    private Long runTotalCount;
    /**
     * 自动执行成功次数
     */
    @Column(name = "RUN_SUCCESS_COUNT")
    private Long runSuccessCount;
    /**
     * 失败原因及修复情况
     */
    @Column(name = "RUN_FAIL_INFO")
    private String runFailInfo;
    /**
     * 接口目标数
     */
    @Column(name = "API_TARGET_COUNT")
    private Long apiTargetCount;
    /**
     * 接口完成数据
     */
    @Column(name = "API_FINISH_COUNT")
    private Long apiFinishCount;
    /**
     * 项目质量分
     */
    @Column(name = "QUALITY_SCORE")
    private Double qualityScore;
    /**
     * 上线测试任务数
     */
    @Column(name = "ONLINE_TEST_CASES_COUNT")
    private Long onlineTestCasesCount;
    /**
     * 上线测试任务成功数
     */
    @Column(name = "ONLINE_TEST_CASES_SUCCESS_COUNT")
    private Long onlineTestCasesSuccessCount;
    /**
     * 是否删除， 1:删除， 0:有效
     */
    @Column(name = "DELETED")
    private Boolean deleted;
}
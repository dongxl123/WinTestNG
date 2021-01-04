package com.winbaoxian.testng.model.dto;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

/**
 * 项目质量报告(ProjectQualityReportHistory)DTO类
 *
 * @author dongxuanliang252
 * @date 2020-06-24 16:52:19
 */
@Setter
@Getter
public class ProjectQualityReportHistoryDTO implements Serializable {
    
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
     * 开始时间
     */
    private Date startTime;
    /**
     * 结束时间
     */
    private Date endTime;
    /**
     * 日期范围
     */
    private String dateRange;
    /**
     * 项目ID
     */
    private Long projectId;
    /**
     * 测试负责人ID，多个用，隔开
     */
    private String testOwnerIds;
    /**
     * 开发负责人ID，多个用,隔开
     */
    private String devOwnerIds;
    /**
     * 自动执行次数
     */
    private Long runTotalCount;
    /**
     * 自动执行成功次数
     */
    private Long runSuccessCount;
    /**
     * 失败原因及修复情况
     */
    private String runFailInfo;
    /**
     * 接口目标数
     */
    private Long apiTargetCount;
    /**
     * 接口完成数
     */
    private Long apiFinishCount;
    /**
     * 项目质量分
     */
    private Double qualityScore;
    /**
     * 上线测试任务数
     */
    private Long onlineTestCasesCount;
    /**
     * 上线测试任务成功数
     */
    private Long onlineTestCasesSuccessCount;
    /**
     * 是否删除， 1:删除， 0:有效
     */
    private Boolean deleted;
}
package com.winbaoxian.testng.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author dongxuanliang252
 * @date 2019-02-26 14:25
 */
@Entity
@Setter
@Getter
@DynamicUpdate
@DynamicInsert
@EntityListeners(AuditingEntityListener.class)
@Table(name = "TEST_CASES")
public class TestCasesEntity implements Serializable {

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
     * 创建人ID
     */
    @CreatedBy
    @Column(name = "CREATOR_UID")
    private Long creatorUid;

    /**
     * 责任人ID
     */
    @Column(name = "OWNER_UID")
    private Long ownerUid;

    /**
     * 接口名称
     */
    @Column(name = "NAME")
    private String name;

    /**
     * 描述
     */
    @Column(name = "DESCRIPTION")
    private String description;

    /**
     * 环境
     */
    @Column(name = "ENV")
    private String env;

    /**
     * 所属项目
     */
    @Column(name = "PROJECT_ID")
    private Long projectId;

    /**
     * 模块
     */
    @Column(name = "MODULE_ID")
    private Long moduleId;

    /**
     * 上线状态，true:已上线，false:未上线
     */
    @Column(name = "CI_FLAG")
    private Boolean ciFlag;

    /**
     * 基础数据配置，json格式
     */
    @Column(name = "BASE_PARAMS")
    private String baseParams;

    /**
     * 前置动作
     */
    @Column(name = "PRE_ACTIONS")
    private String preActions;

    /**
     * 数据准备配置
     */
    @Column(name = "DATA_PREPARATION_CONFIG")
    private String dataPreparationConfig;

    /**
     * 主流程动作
     */
    @Column(name = "MAIN_ACTIONS")
    private String mainActions;

    /**
     * 后置动作
     */
    @Column(name = "POST_ACTIONS")
    private String postActions;

    /**
     * 上次运行状态
     */
    @Column(name = "LAST_RUN_STATE")
    private Integer lastRunState;
    /**
     * 质量分
     */
    @Column(name = "QUALITY_SCORE")
    private Double qualityScore;
    /**
     * 原始质量分
     */
    @Column(name = "QUALITY_SCORE_ORIGIN")
    private Double qualityScoreOrigin;
    /**
     * 是否删除， 1:删除， 0:有效
     */
    @Column(name = "DELETED")
    private Boolean deleted;
}

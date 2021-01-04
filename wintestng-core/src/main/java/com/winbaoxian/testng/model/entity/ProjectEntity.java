package com.winbaoxian.testng.model.entity;

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
 * (Project)实体类
 *
 * @author dongxuanliang252
 * @date 2020-07-14 14:36:15
 */
@Entity
@Setter
@Getter
@DynamicInsert
@DynamicUpdate
@Table(name = "PROJECT")
public class ProjectEntity implements Serializable {

    private static final long serialVersionUID = 526188337656243749L;
    
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
     * 名称
     */
    @Column(name = "NAME")
    private String name;
    /**
     * 集成状态，true：是 ; false：否
     */
    @Column(name = "INTEGRATION_FLAG")
    private Boolean integrationFlag;
    /**
     * 描述
     */
    @Column(name = "DESCRIPTION")
    private String description;
    /**
     * 配置项目邮件发送地址
     */
    @Column(name = "MAIL_ADDRESS")
    private String mailAddress;
    /**
     * 目标数量
     */
    @Column(name = "TARGET_COUNT")
    private Long targetCount;
    /**
     * 完成数量
     */
    @Column(name = "FINISH_COUNT")
    private Long finishCount;
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
     * 质量分
     */
    @Column(name = "QUALITY_SCORE")
    private Double qualityScore;
    /**
     * git仓库地址
     */
    @Column(name = "GIT_ADDRESS")
    private String gitAddress;
    /**
     * 同步api数据
     */
    @Column(name = "SYNC_API_DATA_FLAG")
    private Boolean syncApiDataFlag;
    /**
     * 是否删除， 1:删除， 0:有效
     */
    @Column(name = "DELETED")
    private Boolean deleted;
}
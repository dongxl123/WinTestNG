package com.winbaoxian.testng.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * (Project)DTO类
 *
 * @author dongxuanliang252
 * @date 2020-07-14 14:36:17
 */
@Setter
@Getter
public class ProjectDTO implements Serializable {
    
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
     * 名称
     */
    private String name;
    /**
     * 集成状态，true：是 ; false：否
     */
    private Boolean integrationFlag;
    /**
     * 描述
     */
    private String description;
    /**
     * 配置项目邮件发送地址
     */
    private String mailAddress;
    /**
     * 目标数量
     */
    private Long targetCount;
    /**
     * 完成数量
     */
    private Long finishCount;
    /**
     * 测试负责人ID，多个用，隔开
     */
    private String testOwnerIds;
    /**
     * 开发负责人ID，多个用,隔开
     */
    private String devOwnerIds;
    /**
     * 质量分
     */
    private Double qualityScore;
    /**
     * git仓库地址
     */
    private String gitAddress;
    /**
     * 同步api数据
     */
    private Boolean syncApiDataFlag;
    /**
     * 是否删除， 1:删除， 0:有效
     */
    private Boolean deleted;
}
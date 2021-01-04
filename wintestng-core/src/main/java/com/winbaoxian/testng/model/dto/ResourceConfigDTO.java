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

import com.winbaoxian.testng.enums.ResourceType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * DATABASE_CONFIG
 *
 * @author bianj
 * @version 1.0.0 2019-02-28
 */
@Setter
@Getter
public class ResourceConfigDTO implements Serializable {
    /**
     * 版本号
     */
    private static final long serialVersionUID = -4484608004648116165L;

    /**  */
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
     * 描述
     */
    private String description;

    /**
     * 资源类型
     */
    private ResourceType resourceType;

    /**
     * 资源配置数据
     */
    private String settings;

    /**
     * 是否删除， 1:删除， 0:有效
     */
    private Boolean deleted;

}
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

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * GLOBAL_PARAMS
 *
 * @author bianj
 * @version 1.0.0 2019-02-28
 */
@Setter
@Getter
public class GlobalParamsDTO implements Serializable {
    /**
     * 版本号
     */
    private static final long serialVersionUID = 6581706608543644695L;

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
    private String key;

    /**
     * 值
     */
    private String value;

    /**
     * 描述
     */
    private String description;

    /**
     * 是否删除， 1:删除， 0:有效
     */
    private Boolean deleted;

}
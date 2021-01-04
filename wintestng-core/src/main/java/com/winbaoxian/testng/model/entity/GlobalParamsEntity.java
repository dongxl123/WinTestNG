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
 * GLOBAL_PARAMS
 *
 * @author bianj
 * @version 1.0.0 2019-02-28
 */
@Entity
@Setter
@Getter
@DynamicInsert
@DynamicUpdate
@Table(name = "GLOBAL_PARAMS")
public class GlobalParamsEntity implements Serializable {
    /**
     * 版本号
     */
    private static final long serialVersionUID = 6581706608543644695L;

    /**  */
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
    @Column(name = "[KEY]")
    private String key;

    /**
     * 值
     */
    @Column(name = "[VALUE]")
    private String value;

    /**
     * 描述
     */
    @Column(name = "DESCRIPTION")
    private String description;

    /**
     * 是否删除， 1:删除， 0:有效
     */
    @Column(name = "DELETED")
    private Boolean deleted;

}
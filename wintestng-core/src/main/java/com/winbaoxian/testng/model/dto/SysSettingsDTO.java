package com.winbaoxian.testng.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * SYS_SETTINGS
 *
 * @author dongxuanliang252
 * @version 1.0.0 2020-3-10 16:42:30
 */

@Setter
@Getter
public class SysSettingsDTO implements Serializable {

    /**
     * 主键
     */
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
package com.winbaoxian.testng.model.dto;

import com.winbaoxian.testng.model.core.action.ActionSetting;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2019-02-26 14:25
 */
@Setter
@Getter
public class ActionTemplateDTO implements Serializable {

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
     * 创建人ID
     */
    private Long creatorUid;

    /**
     * 接口名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 参数
     */
    private List<String> params;

    /**
     * 基础数据配置，json格式
     */
    private String baseParams;

    /**
     * 内容
     */
    private List<ActionSetting> actions;

    /**
     * 结果，json格式
     */
    private String result;

    /**
     * 是否支持并发缓存， true：支持， false:不支持
     */
    private Boolean concurrentCacheSupport;

    /**
     * 是否删除， 1:删除， 0:有效
     */
    private Boolean deleted;

}

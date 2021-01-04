package com.winbaoxian.testng.model.dto;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

/**
 * (Api)DTO类
 *
 * @author dongxuanliang252
 * @date 2020-07-14 15:10:43
 */
@Setter
@Getter
public class ApiDTO implements Serializable {
    
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
     * 地址
     */
    private String apiUrl;
    /**
     * 标题
     */
    private String apiTitle;
    /**
     * 项目ID
     */
    private Long projectId;
    /**
     * 目标标识，true：是 ; false：否
     */
    private Boolean targetFlag;
    /**
     * 是否已完成，true：是 ; false：否
     */
    private Boolean finishFlag;
    /**
     * 重复检查值，用于同步api数据
     */
    private String checkKey;
    /**
     * 是否删除， 1:删除， 0:有效
     */
    private Boolean deleted;
}
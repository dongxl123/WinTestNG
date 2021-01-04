package com.winbaoxian.testng.model.entity;

import com.winbaoxian.testng.constant.WinTestNGConstant;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
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
@Table(name = "ACTION_TEMPLATE")
public class ActionTemplateEntity implements Serializable {

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
     * 参数
     */
    @Column(name = "PARAMS")
    @Convert(converter = ParamsStringConverter.class)
    private String[] params;

    /**
     * 基础数据配置，json格式
     */
    @Column(name = "BASE_PARAMS")
    private String baseParams;

    /**
     * 内容，json格式
     */
    @Column(name = "ACTIONS")
    private String actions;

    /**
     * 结果，json格式
     */
    @Column(name = "RESULT")
    private String result;

    /**
     * 是否支持并发缓存， true：支持， false:不支持
     */
    @Column(name = "CONCURRENT_CACHE_SUPPORT")
    private Boolean concurrentCacheSupport;

    /**
     * 是否删除， 1:删除， 0:有效
     */
    @Column(name = "DELETED")
    private Boolean deleted;

    public static class ParamsStringConverter implements AttributeConverter<String[], String> {

        @Override
        public String convertToDatabaseColumn(String[] attributes) {
            return StringUtils.join(attributes, WinTestNGConstant.CHAR_COMMA);
        }

        @Override
        public String[] convertToEntityAttribute(String dbData) {
            return StringUtils.split(dbData, WinTestNGConstant.CHAR_COMMA);
        }
    }
}

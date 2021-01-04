package com.winbaoxian.testng.model.entity;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;

/**
 * (Api)实体类
 *
 * @author dongxuanliang252
 * @date 2020-07-14 15:10:43
 */
@Entity
@Setter
@Getter
@DynamicInsert
@DynamicUpdate
@Table(name = "API")
public class ApiEntity implements Serializable {

    private static final long serialVersionUID = 772705441055437902L;
    
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
     * 地址
     */
    @Column(name = "API_URL")
    private String apiUrl;
    /**
     * 标题
     */
    @Column(name = "API_TITLE")
    private String apiTitle;
    /**
     * 项目ID
     */
    @Column(name = "PROJECT_ID")
    private Long projectId;
    /**
     * 目标标识，true：是 ; false：否
     */
    @Column(name = "TARGET_FLAG")
    private Boolean targetFlag;
    /**
     * 是否已完成，true：是 ; false：否
     */
    @Column(name = "FINISH_FLAG")
    private Boolean finishFlag;
    /**
     * 重复检查值，用于同步api数据
     */
    @Column(name = "CHECK_KEY")
    private String checkKey;
    /**
     * 是否删除， 1:删除， 0:有效
     */
    @Column(name = "DELETED")
    private Boolean deleted;
}
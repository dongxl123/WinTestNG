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

import com.winbaoxian.testng.model.core.DataPreparationConfigDTO;
import com.winbaoxian.testng.model.core.action.ActionSetting;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * TEST_CASES
 *
 * @author bianj
 * @version 1.0.0 2019-02-26
 */
@Setter
@Getter
public class TestCasesDTO implements Serializable {
    /**
     * 版本号
     */
    private static final long serialVersionUID = 5133951644495268810L;

    /**
     *
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
     * 创建人ID
     */
    private Long creatorUid;

    /**
     * 责任人ID
     */
    private Long ownerUid;

    /**
     * 接口名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 环境
     */
    private String env;

    /**
     * 所属项目
     */
    private Long projectId;

    /**
     * 模块
     */
    private Long moduleId;

    /**
     * 上线状态，true:已上线，false:未上线
     */
    private Boolean ciFlag;
    /**
     * 基础数据配置(可参数化)
     */
    private String baseParams;
    /**
     * 前置动作(可参数化)
     */
    private List<ActionSetting> preActions;

    /**
     * 数据准备配置(可参数化)
     */
    private DataPreparationConfigDTO dataPreparationConfig;

    /**
     * 执行主流程动作(http、dubbo等，可参数化)
     */
    private List<ActionSetting> mainActions;

    /**
     * 后置动作
     */
    private List<ActionSetting> postActions;

    /**
     * 上次运行状态
     */
    private Integer lastRunState;
    /**
     * 质量分
     */
    private Double qualityScore;
    /**
     * 原始质量分
     */
    private Double qualityScoreOrigin;
    /**
     * 是否删除， 1:删除， 0:有效
     */
    private Boolean deleted;


}
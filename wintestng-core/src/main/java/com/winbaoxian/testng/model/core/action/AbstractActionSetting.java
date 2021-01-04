package com.winbaoxian.testng.model.core.action;

import com.winbaoxian.testng.enums.ActionType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractActionSetting implements ActionSetting {

    /**
     * 步骤名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 动作类型
     */
    private ActionType actionType;

    /**
     * 延迟时间（单位毫秒）
     */
    private Long delayTimes;

    /**
     * 禁用标识, 默认false
     */
    private Boolean disableFlag;

}

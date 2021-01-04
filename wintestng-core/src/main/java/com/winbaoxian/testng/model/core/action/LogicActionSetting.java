package com.winbaoxian.testng.model.core.action;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LogicActionSetting extends AbstractActionSetting {

    /**
     * 子步骤列表
     */
    private List<ActionSetting> stepList;

}

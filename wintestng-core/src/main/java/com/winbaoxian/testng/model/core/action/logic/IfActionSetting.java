package com.winbaoxian.testng.model.core.action.logic;

import com.winbaoxian.testng.model.core.action.LogicActionSetting;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IfActionSetting extends LogicActionSetting {

    /**
     * 判断条件
     */
    private String condition;

}

package com.winbaoxian.testng.model.core.action.logic;

import com.winbaoxian.testng.model.core.action.LogicActionSetting;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForActionSetting extends LogicActionSetting {

    /**
     * 遍历数据
     */
    private String iterData;

    /**
     * 遍历别名
     */
    private String iterAlias;

}

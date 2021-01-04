package com.winbaoxian.testng.model.core.action;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class NormalActionSetting extends AbstractActionSetting {

    /**
     * 返回结果定义的别名
     */
    private String alias;

}

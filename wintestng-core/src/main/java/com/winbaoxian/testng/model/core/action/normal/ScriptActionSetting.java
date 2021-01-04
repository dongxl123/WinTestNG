package com.winbaoxian.testng.model.core.action.normal;

import com.winbaoxian.testng.enums.ScriptLang;
import com.winbaoxian.testng.model.core.action.NormalActionSetting;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2020-7-20 10:39:25
 */
@Getter
@Setter
public class ScriptActionSetting extends NormalActionSetting {

    /**
     * 脚本语言
     */
    private ScriptLang lang;
    /**
     * 脚本内容
     */
    private String content;
    /**
     * 提取变量列表
     */
    private List<String> extractVars;

}

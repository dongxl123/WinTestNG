package com.winbaoxian.testng.core.script;

import com.winbaoxian.testng.model.core.action.normal.ScriptActionSetting;

/**
 * @author dongxuanliang252
 * @date 2020-07-20 14:48
 */
public interface IScriptLangRunner {

    Object execute(ScriptActionSetting setting);

}

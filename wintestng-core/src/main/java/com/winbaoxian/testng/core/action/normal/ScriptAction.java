package com.winbaoxian.testng.core.action.normal;

import com.winbaoxian.testng.core.action.IAction;
import com.winbaoxian.testng.core.script.ScriptExecutor;
import com.winbaoxian.testng.model.core.TestCasesRunContext;
import com.winbaoxian.testng.model.core.action.normal.ScriptActionSetting;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author dongxuanliang252
 * @date 2020-07-20 14:34
 */
@Component
public class ScriptAction implements IAction<ScriptActionSetting> {

    @Resource
    private ScriptExecutor scriptExecutor;

    @Override
    public Object execute(ScriptActionSetting actionSetting, TestCasesRunContext context) throws Exception {
        return scriptExecutor.execute(actionSetting);
    }

}

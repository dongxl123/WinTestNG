package com.winbaoxian.testng.core.action.logic;

import com.winbaoxian.testng.core.action.IAction;
import com.winbaoxian.testng.model.core.TestCasesRunContext;
import com.winbaoxian.testng.model.core.action.LogicActionSetting;

/**
 * @author dongxuanliang252
 * @date 2020-07-23 12:21
 */
public abstract class AbstractLogicAction<T extends LogicActionSetting> implements IAction<T> {

    @Override
    public Object execute(T actionSetting, TestCasesRunContext context) throws Exception {
        context.pushActionSetting(actionSetting);
        try {
            return executeInternal(actionSetting, context);
        } catch (Exception e) {
            throw e;
        } finally {
            context.popActionSetting();
        }
    }

    protected abstract Object executeInternal(T actionSetting, TestCasesRunContext context) throws Exception;

}

package com.winbaoxian.testng.core.action;

import com.winbaoxian.testng.model.core.TestCasesRunContext;
import com.winbaoxian.testng.model.core.action.ActionSetting;

/**
 * @author dongxuanliang252
 * @date 2019-03-05 14:17
 */
public interface IAction<T extends ActionSetting> {

    Object execute(T actionSetting, TestCasesRunContext context) throws Exception;

}

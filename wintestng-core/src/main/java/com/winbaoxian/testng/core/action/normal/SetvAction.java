package com.winbaoxian.testng.core.action.normal;

import com.winbaoxian.testng.core.action.IAction;
import com.winbaoxian.testng.model.core.TestCasesRunContext;
import com.winbaoxian.testng.model.core.action.normal.SetvActionSetting;
import org.springframework.stereotype.Component;

/**
 * @author dongxuanliang252
 * @date 2019-03-06 17:46
 */
@Component
public class SetvAction implements IAction<SetvActionSetting> {

    @Override
    public Object execute(SetvActionSetting actionSetting, TestCasesRunContext context) throws Exception {
        return actionSetting.getParams();
    }
}

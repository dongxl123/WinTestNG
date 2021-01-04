package com.winbaoxian.testng.model.core.action.normal;

import com.winbaoxian.testng.model.core.action.NormalActionSetting;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

/**
 * @author dongxuanliang252
 * @date 2019-03-11 9:43
 */
@Setter
@Getter
public class AssertionActionSetting extends NormalActionSetting {

    /**
     * 断言配置
     */
    private ArrayList<AssertVerifyConfigDTO> verifyList;

}

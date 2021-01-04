package com.winbaoxian.testng.model.core.action.normal;

import com.winbaoxian.testng.model.core.action.NormalActionSetting;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author dongxuanliang252
 * @date 2019-03-06 17:46
 */
@Setter
@Getter
public class SetvActionSetting extends NormalActionSetting {

    /**
     * kv数据
     */
    private Map<String, Object> params;

}

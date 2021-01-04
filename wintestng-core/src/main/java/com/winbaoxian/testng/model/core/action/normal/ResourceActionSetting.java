package com.winbaoxian.testng.model.core.action.normal;

import com.winbaoxian.testng.model.core.action.NormalActionSetting;
import lombok.Getter;
import lombok.Setter;

/**
 * @author dongxuanliang252
 * @date 2019-03-05 14:10
 */
@Getter
@Setter
public class ResourceActionSetting extends NormalActionSetting {

    private Long resourceId;
    private String sql;
    private boolean fetchOne;
}

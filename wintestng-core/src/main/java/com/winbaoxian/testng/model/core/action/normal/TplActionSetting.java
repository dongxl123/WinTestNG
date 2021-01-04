package com.winbaoxian.testng.model.core.action.normal;

import com.winbaoxian.testng.model.core.action.NormalActionSetting;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;

/**
 * @author dongxuanliang252
 * @date 2019-03-05 14:10
 */
@Getter
@Setter
public class TplActionSetting extends NormalActionSetting {

    /**
     * 模板ID
     */
    private Long templateId;
    /**
     * 映射数据
     */
    private LinkedHashMap<String, Object> mappings;

}

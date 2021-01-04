package com.winbaoxian.testng.model.core.action.normal;

import com.winbaoxian.testng.enums.AssertionType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author dongxuanliang252
 * @date 2019-03-01 14:19
 */
@Getter
@Setter
public class AssertVerifyConfigDTO implements Serializable {

    private AssertionType type;
    private String value1;
    private String value2;

    public String getValue2() {
        if (AssertionType.condition.equals(type)) {
            return null;
        }
        return value2;
    }

}

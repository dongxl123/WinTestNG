package com.winbaoxian.testng.model.core;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author dongxuanliang252
 * @date 2019-03-01 14:18
 */
@Getter
@Setter
public class ParamConfigDTO implements Serializable {

    private Integer paramType;
    private Long databaseConfigId;
    private String query;
    private String key;
    private String value;
}

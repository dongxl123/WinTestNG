package com.winbaoxian.testng.model.core.resource;

import com.winbaoxian.testng.enums.RedisSupportCommandType;
import lombok.Getter;
import lombok.Setter;

/**
 * @author dongxuanliang252
 */
@Setter
@Getter
public class RedisCommandDTO {

    private RedisSupportCommandType commandType;
    private String[] args;

}

package com.winbaoxian.testng.model.core.resource;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dongxuanliang252
 * @date 2019-03-05 17:53
 */
@Getter
@Setter
public class RedisResourceSettings implements ResourceSettings {
    /**
     * 连接地址
     */
    private String host;

    /**
     * 端口
     */
    private int port;

    /**
     * 数据库名
     */
    private int database;

    /**
     * 密码
     */
    private String password;
}

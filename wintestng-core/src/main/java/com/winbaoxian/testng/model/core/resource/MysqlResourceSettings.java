package com.winbaoxian.testng.model.core.resource;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dongxuanliang252
 * @date 2019-03-05 17:53
 */
@Getter
@Setter
public class MysqlResourceSettings implements ResourceSettings {
    /**
     * 连接地址
     */
    private String url;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String password;
}

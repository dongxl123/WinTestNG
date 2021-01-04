package com.winbaoxian.testng.utils;

import java.util.UUID;

/**
 * UUID工具类
 *
 * @Author DongXL
 * @Create 2018-02-05 14:17
 */
public enum UUIDUtil {

    INSTANCE;

    public String randomUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}

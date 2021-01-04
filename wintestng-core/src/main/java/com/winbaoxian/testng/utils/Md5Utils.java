package com.winbaoxian.testng.utils;

import com.alibaba.fastjson.JSON;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

public enum Md5Utils {

    INSTANCE;

    public String md5(Object o) {
        if (o == null) {
            return null;
        }
        String str = JSON.toJSONString(o);
        return StringUtils.upperCase(DigestUtils.md5Hex(str));
    }

}

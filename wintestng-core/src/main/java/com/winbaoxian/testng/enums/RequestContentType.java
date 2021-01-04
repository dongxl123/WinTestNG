package com.winbaoxian.testng.enums;


import org.apache.commons.lang3.StringUtils;

/**
 * @author dongxuanliang252
 * @date 2019-02-26 11:02
 */
public enum RequestContentType {

    XML("application/xml"),
    JSON("application/json"),
    FORM("application/x-www-form-urlencoded"),
    MULTIPART("multipart/form-params"),
    BINARY("application/octet-stream"),
    TEXT("text/plain"),
    EB("e/b"),
    ;

    private String name;

    RequestContentType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static RequestContentType getRequestContentType(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        for (RequestContentType contentType : RequestContentType.values()) {
            if (contentType.getName().equals(name)) {
                return contentType;
            }
        }
        return null;
    }

}

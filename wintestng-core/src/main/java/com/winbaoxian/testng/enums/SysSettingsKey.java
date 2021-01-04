package com.winbaoxian.testng.enums;

public enum SysSettingsKey {

    sendcloudApiUser("sendcloud.api.user"),
    sendcloudApiKey("sendcloud.api.key"),
    sendcloudApiUrl("sendcloud.api.url"),
    ;

    private String keyName;

    SysSettingsKey(String keyName) {
        this.keyName = keyName;
    }

    public String getKeyName() {
        return keyName;
    }

}

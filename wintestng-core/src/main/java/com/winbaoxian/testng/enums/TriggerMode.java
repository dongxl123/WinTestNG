package com.winbaoxian.testng.enums;

public enum TriggerMode {

    AUTO(1, "自动"),
    MANUAL(2, "人工"),
    SCRIPT(3, "脚本"),
    CRON(4, "定时"),
    ;

    private Integer value;
    private String title;

    TriggerMode(Integer value, String title) {
        this.value = value;
        this.title = title;
    }

    public Integer getValue() {
        return value;
    }

    public String getTitle() {
        return title;
    }

    public static TriggerMode getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (TriggerMode triggerMode : TriggerMode.values()) {
            if (triggerMode.value.equals(value)) {
                return triggerMode;
            }
        }
        return null;
    }

}

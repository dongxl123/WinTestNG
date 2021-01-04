package com.winbaoxian.testng.enums;

public enum RunState {

    SUCCESS(1, "成功"),
    FAIL(2, "失败");

    private Integer value;
    private String title;

    RunState(Integer value, String title) {
        this.value = value;
        this.title = title;
    }

    public Integer getValue() {
        return value;
    }

    public String getTitle() {
        return title;
    }

    public static RunState getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (RunState runState : RunState.values()) {
            if (runState.value.equals(value)) {
                return runState;
            }
        }
        return null;
    }

}

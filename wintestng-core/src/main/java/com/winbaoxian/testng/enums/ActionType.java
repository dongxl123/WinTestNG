package com.winbaoxian.testng.enums;

/**
 * @author dongxuanliang252
 * @date 2019-03-05 13:43
 */
public enum ActionType {

    HTTP("HTTP请求", false),
    RESOURCE("资源操作", false),
    SETV("常量设置", false),
    ASSERTION("断言", false),
    TPL("调用模板", false),
    SCRIPT("脚本", false),
    GROUP("分组", true),
    IF("IF判断", true),
    FOR("FOR循环", true),
    ;

    private String title;
    private Boolean logicFlag;

    ActionType(String title, Boolean logicFlag) {
        this.title = title;
        this.logicFlag = logicFlag;
    }

    public String getTitle() {
        return title;
    }

    public Boolean getLogicFlag() {
        return logicFlag;
    }

    public static ActionType getActionType(String type) {
        for (ActionType actionType : ActionType.values()) {
            if (actionType.name().equalsIgnoreCase(type)) {
                return actionType;
            }
        }
        return null;
    }

}

package com.winbaoxian.testng.enums;

/**
 * 报表失败原因
 */
public enum ReportFailReason {

    REASON_1(1, "开发BUG", true),
    REASON_7(7, "接口变更", true),
    REASON_2(2, "脚本不完善", true),
    REASON_3(3, "脏数据问题", true),
    REASON_4(4, "自动化后台问题", true),
    REASON_5(5, "依赖接口问题", true),
    REASON_6(6, "部署问题", false),
    ;
    /**
     * 值
     */
    private Integer value;
    /**
     * 名称
     */
    private String title;
    /**
     * 是否需要选择修复状态
     */
    private Boolean needFixFlag;

    ReportFailReason(Integer value, String title, Boolean needFixFlag) {
        this.value = value;
        this.title = title;
        this.needFixFlag = needFixFlag;
    }

    public Integer getValue() {
        return value;
    }

    public String getTitle() {
        return title;
    }

    public Boolean getNeedFixFlag() {
        return needFixFlag;
    }

}

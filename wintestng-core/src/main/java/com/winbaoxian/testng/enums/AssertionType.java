package com.winbaoxian.testng.enums;

/**
 * @author dongxuanliang252
 * @date 2019-03-07 9:38
 */
public enum AssertionType {

    condition("数学表达式"), eq("相等"), ne("不相等"), contains("包含"), notcontains("不包含"), regex("正则匹配");

    private String title;

    AssertionType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}

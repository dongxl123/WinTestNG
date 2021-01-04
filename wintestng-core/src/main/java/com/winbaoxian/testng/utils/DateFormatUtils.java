package com.winbaoxian.testng.utils;

import org.apache.commons.lang3.time.FastDateFormat;

import java.util.Date;

/**
 * @author dongxuanliang252
 * @date 2019-03-27 12:19
 */
public enum DateFormatUtils {

    INSTANCE;

    private FastDateFormat PATTERN = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.S");
    private FastDateFormat YMD_PATTERN = FastDateFormat.getInstance("yyyyMMdd");

    public String format(Date date) {
        return org.apache.commons.lang3.time.DateFormatUtils.format(date, PATTERN.getPattern());
    }

    public String ymdFormat(Date date) {
        return org.apache.commons.lang3.time.DateFormatUtils.format(date, YMD_PATTERN.getPattern());
    }

}

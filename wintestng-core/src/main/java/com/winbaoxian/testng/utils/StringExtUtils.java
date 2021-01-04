package com.winbaoxian.testng.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum StringExtUtils {

    INSTANCE;

    public String replaceLast(String text, String regex, String replacement) {
        if (text == null || regex == null || replacement == null) {
            return text;
        }
        Pattern p = Pattern.compile(regex);
        Matcher matcher = p.matcher(text);
        int start = 0;
        int end = 0;
        while (matcher.find()) {
            start = matcher.start();
            end = matcher.end();
        }
        if (start > 0 && end > start) {
            return text.substring(0, start) + text.substring(start, end).replaceAll(regex, replacement) + text.substring(end);
        }
        return text;
    }

}

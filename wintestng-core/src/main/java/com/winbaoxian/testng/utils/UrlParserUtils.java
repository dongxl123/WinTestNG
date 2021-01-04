package com.winbaoxian.testng.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum UrlParserUtils {

    INSTANCE;

    private static final Logger log = LoggerFactory.getLogger(UrlParserUtils.class);

    private static final char Q_CHAR = '?';
    private static final char AND_CHAR = '&';
    private static final char EQ_CHAR = '=';
    private static final char SEPARATOR_CHAR = '/';
    private static final String HTTP_STRING = "http";
    private static final String URL_PATTERN = "^http[s]?://[^/]+/(.+)$";


    public String parseRequestUri(String url) {
        if (StringUtils.isBlank(url)) {
            return null;
        }
        String[] parts = StringUtils.split(url, Q_CHAR);
        return StringUtils.trim(parts[0]);
    }

    public Map<String, String> parseRequestParameters(String url) {
        if (StringUtils.isBlank(url)) {
            return null;
        }
        String[] parts = StringUtils.split(url, Q_CHAR);
        if (parts.length < 2) {
            return null;
        }
        String[] params = StringUtils.split(parts[1], AND_CHAR);
        if (params.length < 1) {
            return null;
        }
        Map<String, String> retMap = new HashMap<>();
        try {
            for (String param : params) {
                String[] pvs = StringUtils.split(param, EQ_CHAR);
                if (pvs.length < 1) {
                    continue;
                }
                if (pvs.length < 2) {
                    retMap.put(pvs[0], StringUtils.EMPTY);
                } else {
                    retMap.put(pvs[0], URLDecoder.decode(pvs[1], "utf-8"));
                }
            }
        } catch (UnsupportedEncodingException e) {
            log.error("UnsupportedEncoding", e);
        }
        return retMap;
    }

    public String parseStrictRequestPath(String url) {
        if (StringUtils.isBlank(url)) {
            return null;
        }
        url = parseRequestUri(url);
        if (StringUtils.startsWithIgnoreCase(url, HTTP_STRING)) {
            Pattern pattern = Pattern.compile(URL_PATTERN);
            Matcher matcher = pattern.matcher(url);
            while (matcher.find()) {
                url = matcher.group(1);
            }
        }
        url = StringUtils.replace(url, StringUtils.SPACE, StringUtils.EMPTY);
        String[] urlParts = StringUtils.split(url, SEPARATOR_CHAR);
        return SEPARATOR_CHAR + StringUtils.join(urlParts, SEPARATOR_CHAR);
    }

}

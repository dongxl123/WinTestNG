package com.winbaoxian.testng.utils;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * HTTP请求工具类
 *
 * @Author DongXL
 * @Create 2017-11-10 14:55
 */
public enum HttpUtils {
    INSTANCE;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public static final String LOCAL_HOST_IP = "127.0.0.1";

    public String doPost(String url, String str) throws Exception {
        return doPost(url, str, null, null, null);
    }

    public String doPost(String url, String str, String charSet, Integer timeOutSec, String contentType) throws Exception {
        if (StringUtils.isBlank(charSet)) {
            charSet = "UTF-8";
        }

        if (timeOutSec == null) {
            timeOutSec = Integer.valueOf(10);
        }

        if (StringUtils.isBlank(contentType)) {
            contentType = "application/json";
        }

        String result = null;
        RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout(timeOutSec.intValue() * 1000).setConnectTimeout(timeOutSec.intValue() * 1000).setConnectionRequestTimeout(timeOutSec.intValue() * 1000).build();
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig).build();
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpResponse response = null;

        try {
            ByteArrayEntity byteArrayEntity = null;
            byteArrayEntity = new ByteArrayEntity(str.getBytes());
            byteArrayEntity.setContentType(contentType);
            httpPost.setEntity(byteArrayEntity);
            logger.debug("executing request " + httpPost.getURI());
            response = httpClient.execute(httpPost);
            int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    result = EntityUtils.toString(entity, charSet);
                }
            } else {
                logger.warn("Unexpected response status: {}", Integer.valueOf(status));
                throw new Exception("请求出错，请检查");
            }
            logger.debug("Response content: " + result);
        } catch (Exception var23) {
            logger.warn("httpClient 请求异常", var23);
            throw var23;
        } finally {
            try {
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException var22) {
                logger.warn("流关闭异常", var22);
            }

            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException var21) {
                logger.warn("流关闭异常", var21);
            }

        }

        return result;
    }

    public String doGet(String url) {
        return doGet(url, null);
    }

    public String doGet(String url, Integer timeOutSec) {
        if (timeOutSec == null) {
            timeOutSec = Integer.valueOf(10);
        }
        String result = null;
        CloseableHttpResponse response = null;
        RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout(timeOutSec.intValue() * 1000).setConnectTimeout(timeOutSec.intValue() * 1000).setConnectionRequestTimeout(timeOutSec.intValue() * 1000).build();
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig).build();
        try {
            HttpGet httpget = new HttpGet(url);
            response = httpClient.execute(httpget);
            int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    result = EntityUtils.toString(entity);
                    logger.debug("Response content: " + result);
                }
            } else {
                throw new Exception("请求出错，请检查");
            }
        } catch (Exception var19) {
            logger.warn("http通讯异常", var19);
        } finally {
            try {
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException var18) {
                logger.warn("httpClient关闭异常", var18);
            }

            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException var17) {
                logger.warn("流关闭异常", var17);
            }

        }

        return result;
    }


    public byte[] doFileGetWithRetry(String url) {
        return doFileGetWithRetry(url, 20, 2);
    }

    public byte[] doFileGetWithRetry(String url, Integer timeOutSec, int retryTimes) {
        byte[] bytes = null;
        int index = 0;
        while (true) {
            try {
                bytes = doFileGet(url, timeOutSec);
            } catch (Exception e) {
            }
            index++;
            if (bytes != null || index > retryTimes) {
                break;
            }
            logger.info("doFileGet failed, retryTimes:{}", index);
        }
        return bytes;
    }

    public byte[] doFileGet(String url) {
        return doFileGet(url, null);
    }

    public byte[] doFileGet(String url, Integer timeOutSec) {
        if (timeOutSec == null) {
            timeOutSec = Integer.valueOf(10);
        }
        byte[] result = null;
        CloseableHttpResponse response = null;
        RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout(timeOutSec.intValue() * 1000).setConnectTimeout(timeOutSec.intValue() * 1000).setConnectionRequestTimeout(timeOutSec.intValue() * 1000).build();
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig).build();
        try {
            HttpGet httpget = new HttpGet(url);
            response = httpClient.execute(httpget);
            int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                result = EntityUtils.toByteArray(entity);
                logger.debug("Response content: " + result);
            } else {
                throw new Exception("请求出错，请检查");
            }
        } catch (Exception var19) {
            logger.warn("http通讯异常", var19);
        } finally {
            try {
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException var18) {
                logger.warn("httpClient关闭异常", var18);
            }

            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException var17) {
                logger.warn("流关闭异常", var17);
            }

        }

        return result;
    }

    public String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if (LOCAL_HOST_IP.equals(ip)) {
                //根据网卡取本机配置的IP
                try {
                    InetAddress inet = InetAddress.getLocalHost();
                    ip = inet.getHostAddress();
                } catch (UnknownHostException e) {
                    logger.warn("UnknownHost", e);
                }
            }
        }
        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ip != null && ip.length() > 15) {
            if (ip.indexOf(",") > 0) {
                ip = ip.substring(0, ip.indexOf(","));
            }
        }
        return ip;
    }

    public boolean isAjax(HttpServletRequest request) {
        String xmlHttpRequest = request.getHeader("X-Requested-With");
        if (StringUtils.isNotBlank(xmlHttpRequest)) {
            return true;
        }
        return false;
    }


    public String toCookieString(Object o) {
        if (o == null) {
            return StringUtils.EMPTY;
        }
        if (ClassUtils.isAssignable(o.getClass(), Map.class)) {
            List<String> list = new ArrayList<>();
            Map cookieMap = (Map) o;
            for (Object key : cookieMap.keySet()) {
                Object value = cookieMap.get(key);
                String keyString = String.format("%s=%s", key.toString(), value.toString());
                list.add(keyString);
            }
            return StringUtils.join(list, ";");
        } else if (ClassUtils.isAssignable(o.getClass(), Collection.class)) {
            return StringUtils.join((Collection) o, ";");
        } else if (ClassUtils.isAssignable(o.getClass(), CharSequence.class)) {
            return o.toString();
        }
        return StringUtils.EMPTY;
    }

}

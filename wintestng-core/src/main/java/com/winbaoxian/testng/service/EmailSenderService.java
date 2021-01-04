package com.winbaoxian.testng.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.winbaoxian.testng.constant.WinTestNGConstant;
import com.winbaoxian.testng.enums.SysSettingsKey;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.nio.charset.Charset;
import java.util.List;

/**
 * 发送邮件服务使用了sendCloud, https://sendcloud.sohu.com/
 * 为了数据安全，配置存在sys_settings表中
 */
@Slf4j
@Service
public class EmailSenderService {

    private static final String DEFAULT_API_USER = "b*s_edm";
    private static final String DEFAULT_API_KEY = "beAH2j*fLOU*Wud6";
    private static final String DEFAULT_API_URL = "http://api.sendcloud.net/apiv2/mail/sendtemplate";

    private String apiUser = DEFAULT_API_USER;
    private String apiKey = DEFAULT_API_KEY;
    private String apiUrl = DEFAULT_API_URL;

    @Resource
    private WinTestNGService winTestNGService;

    @PostConstruct
    public void initConfig() {
        this.apiUser = StringUtils.defaultIfBlank(winTestNGService.getSysValueByKey(SysSettingsKey.sendcloudApiUser.getKeyName()), DEFAULT_API_USER);
        this.apiKey = StringUtils.defaultIfBlank(winTestNGService.getSysValueByKey(SysSettingsKey.sendcloudApiKey.getKeyName()), DEFAULT_API_KEY);
        this.apiUrl = StringUtils.defaultIfBlank(winTestNGService.getSysValueByKey(SysSettingsKey.sendcloudApiUrl.getKeyName()), DEFAULT_API_URL);
    }

    /**
     * 发送邮件
     */
    public void sendMailSync(final String subject, final String content, List<String> tos) {
        final String from = "support@winbaoxian.com";
        final String fromName = "自动化测试";
        if (StringUtils.isBlank(subject) || StringUtils.isBlank(content) || CollectionUtils.isEmpty(tos)) {
            return;
        }
        try {
            log.info("同步发送邮件, subject:{}", subject);
            sendEmailBySendCloud(from, fromName, tos, subject, content);
        } catch (Exception ex) {
            log.info("发送邮件异常", ex);
            ex.printStackTrace();
        }

    }


    private boolean sendEmailBySendCloud(String fromAddress, String fromName, List<String> tos, String subject, String content) throws Exception {
        CloseableHttpClient httpClient = createHttpClient(6000);
        HttpPost httpPost = new HttpPost(apiUrl);
        MultipartEntityBuilder entity = MultipartEntityBuilder.create();
        entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        entity.setCharset(Charset.forName("UTF-8"));
        ContentType TEXT_PLAIN = ContentType.create(ContentType.TEXT_HTML.getMimeType(), Charset.forName("UTF-8"));
        entity.addTextBody("apiUser", apiUser, TEXT_PLAIN);
        entity.addTextBody("apiKey", apiKey, TEXT_PLAIN);
        entity.addTextBody("from", fromAddress, TEXT_PLAIN);
        entity.addTextBody("fromName", fromName, TEXT_PLAIN);
        entity.addTextBody("subject", subject, TEXT_PLAIN);
        entity.addTextBody("to", StringUtils.join(tos, WinTestNGConstant.CHAR_SEMICOLON), TEXT_PLAIN);
        entity.addTextBody("html", content, TEXT_PLAIN);
        httpPost.setEntity(entity.build());
        HttpResponse response = httpClient.execute(httpPost);
        // 处理响应
        boolean success = false;
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            // 正常返回, 解析返回数据
            JSONObject jsonObject = JSONObject.parseObject(EntityUtils.toString(response.getEntity()));
            if (jsonObject.getBoolean("result") && new Integer(200).equals(jsonObject.getInteger("statusCode"))) {
                log.info("send email succeed, email:{} ", JSON.toJSONString(tos));
                success = true;
            } else {
                log.info("send email falied, {}", jsonObject.getString("message"));
            }
        } else {
            log.info("send email failed: http status:{}", response.getStatusLine().getStatusCode());
        }
        httpPost.releaseConnection();
        return success;
    }

    private CloseableHttpClient createHttpClient(int reqTimeout) {
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setSocketTimeout(reqTimeout)
                .setConnectTimeout(reqTimeout)
                .setConnectionRequestTimeout(reqTimeout)
                .build();
        return HttpClients.custom()
                .setDefaultRequestConfig(defaultRequestConfig)
                .build();
    }


}

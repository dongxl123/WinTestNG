package com.winbaoxian.testng.core.action.normal;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.winbaoxian.common.freemarker.utils.JsonUtils;
import com.winbaoxian.testng.constant.WinTestNGConstant;
import com.winbaoxian.testng.core.action.IAction;
import com.winbaoxian.testng.enums.RequestContentType;
import com.winbaoxian.testng.enums.RequestMethod;
import com.winbaoxian.testng.exception.WinTestNgException;
import com.winbaoxian.testng.model.core.TestCasesRunContext;
import com.winbaoxian.testng.model.core.action.normal.HttpActionSetting;
import com.winbaoxian.testng.model.core.log.TestReportDataTestCaseDTO;
import com.winbaoxian.testng.service.AnalysisService;
import com.winbaoxian.testng.utils.HttpUtils;
import com.winbaoxian.testng.utils.UrlParserUtils;
import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static io.restassured.config.EncoderConfig.encoderConfig;
import static io.restassured.config.RedirectConfig.redirectConfig;
import static io.restassured.config.SSLConfig.sslConfig;

/**
 * @author dongxuanliang252
 * @date 2019-03-05 14:25
 */
@Component
@Slf4j
public class HttpAction implements IAction<HttpActionSetting> {

    @Value("${project.filePath:files}")
    private String filePath;
    @Resource
    private AnalysisService analysisService;

    static {
        //默认UTF-8编码、redirect、trust all ssl
        RestAssured.config = RestAssured.config().encoderConfig(encoderConfig().defaultContentCharset(Charset.defaultCharset()))
                .redirect(redirectConfig().followRedirects(false))
                .sslConfig(sslConfig().relaxedHTTPSValidation());
    }

    @Override
    public Object execute(HttpActionSetting actionSetting, TestCasesRunContext context) throws Exception {
        if (actionSetting == null) {
            return null;
        }
        TestReportDataTestCaseDTO reportDataContext = context.getReportDataContext();
        if (BooleanUtils.isTrue(reportDataContext.getAnalysisFlag())) {
            analysisService.analysisApiFinishFlag(context.getTestCasesDTO().getProjectId(), actionSetting.getRequestUrl());
        }
        RequestSpecification specification = given().log().everything();
        //ContentType
        RequestContentType requestContentType = actionSetting.getRequestContentType();
        if (requestContentType != null) {
            specification.contentType(requestContentType.getName());
        } else {
            requestContentType = RequestContentType.TEXT;
            specification.contentType(ContentType.TEXT);
        }
        //RequestHeader
        Map<String, String> requestHeader = actionSetting.getRequestHeader();
        if (MapUtils.isNotEmpty(requestHeader)) {
            specification.headers(requestHeader);
        }
        //RequestParams
        Map<String, String> requestParams = actionSetting.getRequestParams();
        if (MapUtils.isNotEmpty(requestParams)) {
            specification.queryParams(requestParams);
        }
        RestAssuredConfig config = RestAssured.config();
        //RequestBody
        String requestBody = actionSetting.getRequestBody();
        if (StringUtils.isNotBlank(requestBody)) {
            if (Arrays.asList(RequestContentType.XML, RequestContentType.JSON, RequestContentType.TEXT).contains(requestContentType)) {
                specification.body(requestBody);
            } else if (RequestContentType.FORM.equals(requestContentType)) {
                specification.formParams(getFormParams(requestBody));
            } else if (RequestContentType.MULTIPART.equals(requestContentType)) {
                JSONObject multiPartParams = JSON.parseObject(requestBody);
                if (MapUtils.isNotEmpty(multiPartParams)) {
                    if (MapUtils.isNotEmpty(requestParams)) {
                        for (String key : requestParams.keySet()) {
                            specification.multiPart(key, (Object) requestParams.get(key));
                        }
                    }
                    Set<String> keys = multiPartParams.keySet();
                    String k = (String) CollectionUtils.get(keys, 0);
                    String v = multiPartParams.getString(k);
                    String mimeType = RequestContentType.BINARY.getName();
                    String fileName = v.lastIndexOf("/") >= 0 ? v.substring(v.lastIndexOf("/") + 1) : v;
                    Path path = Paths.get(fileName);
                    try {
                        mimeType = Files.probeContentType(path);
                    } catch (IOException e) {
                    }
                    if (StringUtils.startsWithIgnoreCase(v, WinTestNGConstant.STRING_HTTP)) {
                        byte[] bytes = HttpUtils.INSTANCE.doFileGetWithRetry(v);
                        specification.multiPart(k, fileName, bytes, mimeType);
                    } else {
                        File file = new File(filePath + File.separator + v);
                        specification.multiPart(k, file, mimeType);
                    }
                }
            } else if (RequestContentType.BINARY.equals(requestContentType)) {
                // specification.
            } else if (RequestContentType.EB.equals(requestContentType)) {
                config = config.encoderConfig(encoderConfig().defaultCharsetForContentType(Charset.defaultCharset(), ContentType.JSON).encodeContentTypeAs(RequestContentType.EB.getName(), ContentType.JSON));
                specification.body(requestBody);
            } else {
                config = config.encoderConfig(encoderConfig().encodeContentTypeAs(requestContentType.getName(), ContentType.TEXT));
                specification.body(requestBody);
            }
        }
        //设置config
        specification.config(config);
        //RequestMethod
        RequestMethod requestMethod = actionSetting.getRequestMethod();
        Map<String, String> requestParameters = UrlParserUtils.INSTANCE.parseRequestParameters(actionSetting.getRequestUrl());
        if (MapUtils.isNotEmpty(requestParameters)) {
            specification.queryParams(requestParameters);
        }
        Response response = null;
        String requestUri = UrlParserUtils.INSTANCE.parseRequestUri(actionSetting.getRequestUrl());
        if (RequestMethod.POST.equals(requestMethod)) {
            response = specification.post(requestUri);
        } else if (RequestMethod.GET.equals(requestMethod)) {
            response = specification.get(requestUri);
        } else if (RequestMethod.PUT.equals(requestMethod)) {
            response = specification.put(requestUri);
        } else if (RequestMethod.DELETE.equals(requestMethod)) {
            response = specification.delete(requestUri);
        } else {
            response = specification.post(requestUri);
        }
        response.then().log().body();
        JSONObject ret = new JSONObject();
        ret.put("statusCode", response.getStatusCode());
        ret.put("headers", response.getHeaders());
        ret.put("cookies", response.getCookies());
        ret.put("body", JsonUtils.INSTANCE.parseObject(response.asString()));
        return ret;
    }

    private Map<String, String> getFormParams(String requestBody) {
        Object o = JsonUtils.INSTANCE.parseObject(requestBody);
        if (o instanceof JSONObject) {
            Map<String, String> formParams = new HashMap<>();
            JSONObject jo = (JSONObject) o;
            for (String key : jo.keySet()) {
                Object value = jo.get(key);
                if (value instanceof String) {
                    formParams.put(key, (String) value);
                } else {
                    formParams.put(key, JsonUtils.INSTANCE.toJSONString(value));
                }
            }
            return formParams;
        } else if (o instanceof JSONArray) {
            throw new WinTestNgException("requestBody格式不对,请检查");
        } else if (o instanceof String) {
            String kvStrList = (String) o;
            if (StringUtils.isBlank(kvStrList)) {
                return MapUtils.EMPTY_MAP;
            }
            String[] parameterParts = StringUtils.split(kvStrList, "&");
            if (parameterParts.length < 1) {
                return MapUtils.EMPTY_MAP;
            }
            Map<String, String> formParams = new HashMap<>();
            for (String parameterPart : parameterParts) {
                String[] ps = StringUtils.split(parameterPart, "=");
                if (ps.length < 1) {
                    continue;
                }
                if (ps.length < 2) {
                    formParams.put(StringUtils.trim(ps[0]), StringUtils.EMPTY);
                } else {
                    try {
                        formParams.put(StringUtils.trim(ps[0]), URLDecoder.decode(StringUtils.trim(ps[1]), "utf-8"));
                    } catch (UnsupportedEncodingException e) {
                        log.error("getFormParams异常", e);
                    }
                }
            }
            return formParams;
        }
        return MapUtils.EMPTY_MAP;
    }

}

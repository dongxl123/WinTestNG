package com.winbaoxian.testng.model.core.action.normal;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.winbaoxian.testng.enums.RequestContentType;
import com.winbaoxian.testng.enums.RequestMethod;
import com.winbaoxian.testng.model.core.action.NormalActionSetting;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * @author dongxuanliang252
 * @date 2019-03-05 13:41
 */
@Setter
@Getter
public class HttpActionSetting extends NormalActionSetting {
    /**
     * 请求头
     */
    private Map<String, String> requestHeader;

    /**
     * 请求地址
     */
    private String requestUrl;

    /**
     * 请求参数
     */
    private Map<String, String> requestParams;

    /**
     * 请求方式
     */
    private RequestMethod requestMethod;

    /**
     * 请求数据类型
     */
    @JSONField(serializeUsing = RequestContentTypeSerializer.class, deserializeUsing = RequestContentTypeDeserializer.class)
    private RequestContentType requestContentType;

    /**
     * 请求体
     */
    private String requestBody;

    public static class RequestContentTypeSerializer implements ObjectSerializer {
        @Override
        public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType,
                          int features) throws IOException {
            if (object != null) {
                RequestContentType value = (RequestContentType) object;
                serializer.write(value.getName());
            } else {
                serializer.writeNull();
            }
        }
    }

    public static class RequestContentTypeDeserializer implements ObjectDeserializer {

        @Override
        public RequestContentType deserialze(DefaultJSONParser parser, Type fieldType, Object fieldName) {
            String value = parser.getLexer().stringVal();
            if (StringUtils.isBlank(value)) {
                return null;
            }
            return RequestContentType.getRequestContentType(value);
        }

        @Override
        public int getFastMatchToken() {
            return 0;
        }
    }

}



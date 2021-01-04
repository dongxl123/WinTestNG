package com.winbaoxian.testng.model.core.resource;

import com.winbaoxian.testng.enums.MongoSupportCommandType;
import lombok.Getter;
import lombok.Setter;
import org.bson.BsonArray;
import org.bson.conversions.Bson;

/**
 * @author dongxuanliang252
 */
@Setter
@Getter
public class MongoCommandDTO {

    private String prefix;
    private String collection;
    private MongoSupportCommandType commandType;
    private BsonArray args;
    /**
     * MongoSupportFunctionType为find命令时使用以下参数
     */
    private Integer skip;
    private Integer limit;
    private Bson sort;
}

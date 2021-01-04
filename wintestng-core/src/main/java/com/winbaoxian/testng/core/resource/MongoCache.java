package com.winbaoxian.testng.core.resource;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClientURI;
import com.mongodb.client.*;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.winbaoxian.common.freemarker.utils.JsonUtils;
import com.winbaoxian.testng.constant.WinTestNGConstant;
import com.winbaoxian.testng.enums.MongoSupportCommandType;
import com.winbaoxian.testng.exception.WinTestNgException;
import com.winbaoxian.testng.model.core.resource.MongoCommandDTO;
import com.winbaoxian.testng.model.core.resource.MongoResourceSettings;
import com.winbaoxian.testng.utils.MongoShellUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.BsonValue;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author dongxuanliang252
 * @date 2019-03-01 15:46
 */

public class MongoCache extends AbstractResourceCache<MongoClient> implements INoSqlOperation {

    private static final String KEY_ITEM_BATCH = "itemBatch";
    private static final String KEY_SET = "$set";
    private static final String KEY_PLACE_HOLDER = "$";
    private static final String FIELD_NAME_ID = "_id";

    private Logger log = LoggerFactory.getLogger(getClass());

    private MongoClient mongoClient;
    private MongoClientURI uri;

    public MongoCache(MongoResourceSettings setting) {
        this.mongoClient = MongoClients.create(setting.getUrl());
        this.uri = new MongoClientURI(setting.getUrl());
        this.settings = setting;
    }

    @Override
    public MongoClient getCacheObject() {
        return mongoClient;
    }

    /**
     * @param command
     * @return
     */
    @Override
    public Object execute(String command) {
        MongoCommandDTO mongoCommandDTO = MongoShellUtils.INSTANCE.parseShellCommand(command);
        log.info("parse MongoCommandDTO: {}", JsonUtils.INSTANCE.toJSONString(mongoCommandDTO));
        MongoDatabase mongoDatabase = mongoClient.getDatabase(uri.getDatabase());
        MongoCollection<Document> collection = mongoDatabase.getCollection(mongoCommandDTO.getCollection());
        MongoSupportCommandType commandType = mongoCommandDTO.getCommandType();
        BsonArray args = mongoCommandDTO.getArgs();
        if (Arrays.asList(MongoSupportCommandType.count, MongoSupportCommandType.countDocuments).contains(commandType)) {
            if (CollectionUtils.isEmpty(args)) {
                return collection.countDocuments();
            } else {
                Bson filter = (Bson) args.get(0);
                return collection.countDocuments(filter);
            }
        } else if (MongoSupportCommandType.find.equals(commandType)) {
            FindIterable<Document> rsIter = null;
            if (CollectionUtils.isEmpty(args)) {
                rsIter = collection.find();
            } else {
                Bson filter = (Bson) args.get(0);
                rsIter = collection.find(filter);
            }
            if (mongoCommandDTO.getSort() != null) {
                rsIter.sort(mongoCommandDTO.getSort());
            }
            if (mongoCommandDTO.getSkip() != null) {
                rsIter.skip(mongoCommandDTO.getSkip());
            }
            if (mongoCommandDTO.getLimit() != null) {
                rsIter.limit(mongoCommandDTO.getLimit());
            }
            return MongoShellUtils.INSTANCE.readResultList(rsIter);
        } else if (MongoSupportCommandType.findOne.equals(commandType)) {
            FindIterable<Document> rsIter = null;
            if (CollectionUtils.isEmpty(args)) {
                rsIter = collection.find();
            } else {
                Bson filter = (Bson) args.get(0);
                rsIter = collection.find(filter);
            }
            if (mongoCommandDTO.getSort() != null) {
                rsIter.sort(mongoCommandDTO.getSort());
            }
            if (mongoCommandDTO.getSkip() != null) {
                rsIter.skip(mongoCommandDTO.getSkip());
            }
            rsIter = rsIter.limit(1);
            return MongoShellUtils.INSTANCE.readOneResult(rsIter);
        } else if (MongoSupportCommandType.insert.equals(commandType)) {
            if (CollectionUtils.isEmpty(args)) {
                throw new WinTestNgException(String.format("mongo command(%s) has no documents: %s", commandType, command));
            } else {
                BsonValue bsonValue = args.get(0);
                if (bsonValue.isDocument()) {
                    Document document = Document.parse(bsonValue.asDocument().toJson());
                    collection.insertOne(document);
                } else if (bsonValue.isArray()) {
                    List<Document> documents = new ArrayList<>();
                    for (BsonValue value : bsonValue.asArray()) {
                        Document document = Document.parse(value.asDocument().toJson());
                        documents.add(document);
                    }
                    collection.insertMany(documents);
                }
            }
        } else if (MongoSupportCommandType.insertOne.equals(commandType)) {
            if (CollectionUtils.isEmpty(args)) {
                throw new WinTestNgException(String.format("mongo command(%s) has no document: %s", commandType, command));
            } else {
                Document document = Document.parse(args.get(0).asDocument().toJson());
                collection.insertOne(document);
            }
        } else if (MongoSupportCommandType.insertMany.equals(commandType)) {
            if (CollectionUtils.isEmpty(args)) {
                throw new WinTestNgException(String.format("mongo command(%s) has no documents: %s", commandType, command));
            } else {
                List<Document> documents = new ArrayList<>();
                for (BsonValue value : args.get(0).asArray()) {
                    Document document = Document.parse(value.asDocument().toJson());
                    documents.add(document);
                }
                collection.insertMany(documents);
            }
        } else if (Arrays.asList(MongoSupportCommandType.update, MongoSupportCommandType.updateMany).contains(commandType)) {
            if (CollectionUtils.isEmpty(args) || args.size() < 2) {
                throw new WinTestNgException(String.format("mongo command(%s) is incorrect: %s", commandType, command));
            } else {
                BsonDocument filter = args.get(0).asDocument();
                BsonDocument update = args.get(1).asDocument();
                if (args.size() > 2) {
                    BsonDocument options = args.get(2).asDocument();
                    if (options.containsKey(KEY_ITEM_BATCH) && update.containsKey(KEY_SET)) {
                        BsonDocument updateValues = update.get(KEY_SET).asDocument();
                        collection.find(filter).forEach((Consumer<Document>) document -> {
                            for (String key : updateValues.keySet()) {
                                updateDocument(document, key, updateValues.get(key));
                            }
                            collection.findOneAndReplace(new BasicDBObject(FIELD_NAME_ID, document.get(FIELD_NAME_ID)), document);
                        });
                    }
                } else {
                    UpdateResult result = collection.updateMany(filter, update);
                    return result.getModifiedCount();
                }
            }
        } else if (MongoSupportCommandType.updateOne.equals(commandType)) {
            if (CollectionUtils.isEmpty(args) || args.size() < 2) {
                throw new WinTestNgException(String.format("mongo command(%s) is incorrect: %s", commandType, command));
            } else {
                BsonDocument filter = args.get(0).asDocument();
                BsonDocument update = args.get(1).asDocument();
                UpdateResult result = collection.updateOne(filter, update);
                return result.getModifiedCount();
            }
        } else if (MongoSupportCommandType.deleteOne.equals(commandType)) {
            if (CollectionUtils.isEmpty(args)) {
                throw new WinTestNgException(String.format("mongo command(%s) is incorrect: %s", commandType, command));
            } else {
                Bson filter = (Bson) args.get(0);
                DeleteResult result = collection.deleteOne(filter);
                return result.getDeletedCount();
            }
        } else if (Arrays.asList(MongoSupportCommandType.deleteMany, MongoSupportCommandType.remove).contains(commandType)) {
            if (CollectionUtils.isEmpty(args)) {
                throw new WinTestNgException(String.format("mongo command(%s) is incorrect: %s", commandType, command));
            } else {
                Bson filter = (Bson) args.get(0);
                DeleteResult result = collection.deleteMany(filter);
                return result.getDeletedCount();
            }
        }
        return null;
    }

    private Document updateDocument(Document document, String key, Object v) {
        if (StringUtils.isBlank(key)) {
            return document;
        }
        String[] props = StringUtils.split(key, WinTestNGConstant.CHAR_DOT);
        if (CollectionUtils.size(props) == 1) {
            document.put(key, v);
            return document;
        }
        for (int i = 0; i < props.length - 1; i++) {
            String thisProp = props[i];
            String nextProp = props[i + 1];
            if (KEY_PLACE_HOLDER.equals(thisProp)) {
                continue;
            } else if (KEY_PLACE_HOLDER.equals(nextProp)) {
                String nextKey = StringUtils.join(props, WinTestNGConstant.CHAR_DOT, 2, props.length);
                List<Object> innerDocList = document.get(thisProp, List.class);
                List<Object> newInnerDocList = new ArrayList<>();
                for (Object innerDoc : innerDocList) {
                    if (innerDoc instanceof Document) {
                        updateDocument((Document) innerDoc, nextKey, v);
                    } else {
                        newInnerDocList.add(v);
                    }
                }
                if (CollectionUtils.isNotEmpty(newInnerDocList)) {
                    document.put(thisProp, newInnerDocList);
                }
            } else {
                String nextKey = StringUtils.join(props, WinTestNGConstant.CHAR_DOT, 1, props.length);
                updateDocument(document.get(thisProp, Document.class), nextKey, v);
            }
        }
        return document;
    }

}

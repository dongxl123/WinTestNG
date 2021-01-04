package com.winbaoxian.testng.utils;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.winbaoxian.testng.constant.WinTestNGConstant;
import com.winbaoxian.testng.enums.MongoSupportCommandType;
import com.winbaoxian.testng.exception.WinTestNgException;
import com.winbaoxian.testng.model.core.resource.MongoCommandDTO;
import org.apache.commons.lang3.StringUtils;
import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author dongxuanliang252
 * @date 2019-03-12 10:17
 */
public enum MongoShellUtils {

    INSTANCE;

    private static final String MONGO_COMMAND_PREFIX = "db";
    private static final String MONGO_COMMAND_FUNCTION_REGEX = "^(\\w+)\\((.*?)\\)(?:\\.(limit|sort|skip)\\((.*?)\\))?(?:\\.(limit|sort|skip)\\((.*?)\\))?(?:\\.(limit|sort|skip)\\((.*?)\\))?[\\s\\;]*$";
    private static final String MONGO_COMMAND_SKIP = "skip";
    private static final String MONGO_COMMAND_LIMIT = "limit";
    private static final String MONGO_COMMAND_SORT = "sort";

    /**
     * @param command
     * @return
     */
    public MongoCommandDTO parseShellCommand(String command) {
        String tempCommand = command;
        if (StringUtils.isBlank(tempCommand)) {
            throw new WinTestNgException("mongo command can not be null");
        }
        tempCommand = StringUtils.trim(tempCommand);
        int idx = tempCommand.indexOf(WinTestNGConstant.CHAR_DOT);
        MongoCommandDTO commandDTO = new MongoCommandDTO();
        if (idx == -1) {
            throw new WinTestNgException(String.format("mongo command is error: %s", command));
        } else {
            String prefix = tempCommand.substring(0, idx);
            if (!StringUtils.equalsIgnoreCase(prefix, MONGO_COMMAND_PREFIX)) {
                throw new WinTestNgException(String.format("mongo command prefix is error: %s", command));
            }
            commandDTO.setPrefix(prefix);
            tempCommand = tempCommand.substring(idx + 1);
        }
        idx = tempCommand.indexOf(WinTestNGConstant.CHAR_DOT);
        if (idx == -1) {
            throw new WinTestNgException(String.format("mongo command is error: %s", command));
        } else {
            String collection = tempCommand.substring(0, idx);
            commandDTO.setCollection(collection);
            tempCommand = tempCommand.substring(idx + 1);
        }
        Pattern pattern = Pattern.compile(MONGO_COMMAND_FUNCTION_REGEX, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(tempCommand);
        while (matcher.find()) {
            int count = matcher.groupCount();
            if (count % 2 != 0 || count < 2) {
                throw new WinTestNgException(String.format("mongo command is error: %s", command));
            }
            if (count >= 2) {
                String functionStr = matcher.group(1);
                MongoSupportCommandType commandType = MongoSupportCommandType.getByName(functionStr);
                if (commandType == null) {
                    throw new WinTestNgException(String.format("mongo command does not support this operation(%s): %s", functionStr, command));
                }
                commandDTO.setCommandType(commandType);
                String argsJson = String.format("[%s]", matcher.group(2));
                BsonArray args = BsonArray.parse(argsJson);
                commandDTO.setArgs(args);
                // find command args: sort,skip,limit
                for (int i = 3; i < count; i = i + 2) {
                    String argStr = matcher.group(i);
                    String valueStr = matcher.group(i + 1);
                    if (StringUtils.equalsIgnoreCase(argStr, MONGO_COMMAND_SORT)) {
                        commandDTO.setSort(BsonDocument.parse(valueStr));
                    } else if (StringUtils.equalsIgnoreCase(argStr, MONGO_COMMAND_SKIP)) {
                        commandDTO.setSkip(Integer.valueOf(valueStr));
                    } else if (StringUtils.equalsIgnoreCase(argStr, MONGO_COMMAND_LIMIT)) {
                        commandDTO.setLimit(Integer.valueOf(valueStr));
                    }
                }
            }
        }
        if (commandDTO.getCommandType() == null) {
            throw new WinTestNgException(String.format("mongo command is error: %s", command));
        }
        return commandDTO;
    }

    public List<Document> readResultList(FindIterable<Document> rsIter) {
        List<Document> documents = new ArrayList<>();
        MongoCursor<Document> cursor = rsIter.iterator();
        while (cursor.hasNext()) {
            Document document = cursor.next();
            documents.add(document);
        }
        return documents;
    }

    public Document readOneResult(FindIterable<Document> rsIter) {
        Document document = new Document();
        MongoCursor<Document> cursor = rsIter.iterator();
        if (cursor.hasNext()) {
            document = cursor.next();
        }
        return document;
    }

}

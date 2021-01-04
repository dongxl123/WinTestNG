package com.winbaoxian.testng.enums;

/**
 * @author dongxuanliang252
 * @date 2019-03-12 11:04
 */

public enum MongoSupportCommandType {

    count,
    countDocuments,
    find,
    findOne,
    insert,
    insertOne,
    insertMany,
    update,
    updateOne,
    updateMany,
    deleteOne,
    deleteMany,
    remove;

    public static MongoSupportCommandType getByName(String name){
        for(MongoSupportCommandType commandType: MongoSupportCommandType.values()){
            if(commandType.name().equalsIgnoreCase(name)){
                return commandType;
            }
        }
        return null;
    }

}

package com.winbaoxian.testng.core.resource;

import com.winbaoxian.testng.BaseTest;
import com.winbaoxian.testng.model.core.resource.MongoResourceSettings;
import org.testng.annotations.Test;

/**
 * @author dongxuanliang252
 * @date 2019-03-14 18:42
 */
public class MongoCacheTest extends BaseTest {

    @Test
    public void testExec() {
        MongoResourceSettings setting = new MongoResourceSettings();
        setting.setUrl("mongodb://wyxx_app:nfrqT1dbyzfh8w0irKHEE7wdmkUUM1RO@dtest1:27017,dtest2:3717,test2:3717/wyxx_app");
        MongoCache mongoCache = new MongoCache(setting);
        Object a = mongoCache.execute(" db.user_group_msg_history.update({_id:10608032},{$set:{\"actMsgList.$.isDeleted\":true}},{\"itemBatch\":true})");
        //         Object a = mongoCache.execute(" db.user_group_msg_history.update({_id:10608032},{$set:{\"test.$\":7}},{\"itemBatch\":true})");
    }

}

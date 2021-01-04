package com.winbaoxian.testng.core.resource;

import com.winbaoxian.testng.model.core.resource.MqResourceSettings;

/**
 * @author dongxuanliang252
 * @date 2019-03-01 15:46
 */

public class MqCache extends AbstractResourceCache<Object> {


    public MqCache(MqResourceSettings settings) {
    }

    @Override
    public Object getCacheObject() {
        return null;
    }

}

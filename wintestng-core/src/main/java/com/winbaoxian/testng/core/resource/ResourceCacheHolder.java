package com.winbaoxian.testng.core.resource;

import com.alibaba.fastjson.JSON;
import com.winbaoxian.testng.enums.ResourceType;
import com.winbaoxian.testng.model.core.resource.MongoResourceSettings;
import com.winbaoxian.testng.model.core.resource.MqResourceSettings;
import com.winbaoxian.testng.model.core.resource.MysqlResourceSettings;
import com.winbaoxian.testng.model.core.resource.RedisResourceSettings;
import com.winbaoxian.testng.model.dto.ResourceConfigDTO;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author dongxuanliang252
 * @date 2019-03-01 15:30
 */
public enum ResourceCacheHolder {

    INSTANCE;

    private Map<Long, IResourceCache> cache = new ConcurrentHashMap<>();

    public IResourceCache putAndGetIfAbsent(ResourceConfigDTO config) {
        if (config == null) {
            return null;
        }
        ResourceType dbType = config.getResourceType();
        String str = config.getSettings();
        if (StringUtils.isBlank(str)) {
            return null;
        }
        if (ResourceType.mysql.equals(dbType)) {
            MysqlResourceSettings settings = JSON.parseObject(str, MysqlResourceSettings.class);
            putInCacheIfAbsent(cache, config.getId(), new MysqlCache(settings));
        } else if (ResourceType.mongo.equals(dbType)) {
            MongoResourceSettings settings = JSON.parseObject(str, MongoResourceSettings.class);
            putInCacheIfAbsent(cache, config.getId(), new MongoCache(settings));
        } else if (ResourceType.redis.equals(dbType)) {
            RedisResourceSettings settings = JSON.parseObject(str, RedisResourceSettings.class);
            putInCacheIfAbsent(cache, config.getId(), new RedisCache(settings));
        } else if (ResourceType.mq.equals(dbType)) {
            MqResourceSettings settings = JSON.parseObject(str, MqResourceSettings.class);
            putInCacheIfAbsent(cache, config.getId(), new MqCache(settings));
        }
        return cache.get(config.getId());
    }

    private void putInCacheIfAbsent(Map<Long, IResourceCache> cache, Long key, IResourceCache newValue) {
        IResourceCache old = cache.get(key);
        if (old != null && old.getConfigMd5().equals(newValue.getConfigMd5())) {
            return;
        }
        cache.put(key, newValue);
    }

}

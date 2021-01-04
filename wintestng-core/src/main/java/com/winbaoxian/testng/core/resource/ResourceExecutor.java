package com.winbaoxian.testng.core.resource;

import com.winbaoxian.common.freemarker.utils.JsonUtils;
import com.winbaoxian.testng.model.core.action.normal.ResourceActionSetting;
import com.winbaoxian.testng.model.dto.ResourceConfigDTO;
import com.winbaoxian.testng.service.WinTestNGService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author dongxuanliang252
 * @date 2019-03-01 17:40
 */
@Component
public class ResourceExecutor {

    @Resource
    private WinTestNGService winTestNGService;

    public Object executeDbQuery(ResourceActionSetting setting) {
        if (setting == null || setting.getResourceId() == null) {
            return null;
        }
        ResourceConfigDTO configDTO = winTestNGService.getResourceConfig(setting.getResourceId());
        if (configDTO == null) {
            return null;
        }
        IResourceCache resourceCache = ResourceCacheHolder.INSTANCE.putAndGetIfAbsent(configDTO);
        Object ret = null;
        if (resourceCache instanceof MysqlCache) {
            ret = ((MysqlCache) resourceCache).execute(setting.getSql(), setting.isFetchOne());
        } else if (resourceCache instanceof MongoCache) {
            ret = ((MongoCache) resourceCache).execute(setting.getSql());
        } else if (resourceCache instanceof RedisCache) {
            ret = ((RedisCache) resourceCache).execute(setting.getSql());
        }
        return JsonUtils.INSTANCE.toJSON(ret);
    }

}

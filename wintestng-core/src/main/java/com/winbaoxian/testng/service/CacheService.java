package com.winbaoxian.testng.service;

import com.winbaoxian.testng.constant.WinTestNGConstant;
import com.winbaoxian.testng.model.dto.NormalDistributionDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class CacheService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 清理模板并发缓存
     *
     * @param reportUuid
     * @param keys
     */
    public void clearTplConcurrentCacheForReportUuid(String reportUuid, List<String> keys) {
        if (StringUtils.isBlank(reportUuid) || CollectionUtils.isEmpty(keys)) {
            return;
        }
        for (String key : keys) {
            redisTemplate.boundHashOps(key).delete(reportUuid);
        }
    }

    /**
     * 尝试获取锁
     *
     * @param lockKey
     * @return
     */
    public boolean tryFetchLock(String lockKey) {
        boolean fetchLock = redisTemplate.boundValueOps(lockKey).setIfAbsent(1);
        //成功获取锁，设置超时时间
        if (fetchLock) {
            redisTemplate.expire(lockKey, WinTestNGConstant.TPL_CACHE_LOCK_TIMEOUT, TimeUnit.SECONDS);
        }
        return fetchLock;
    }


    /**
     * 删除锁
     *
     * @param lockKey
     */
    public void removeLock(String lockKey) {
        redisTemplate.delete(lockKey);
    }

    /**
     * 查询模板并发缓存数据; 数据结构 cacheKey: {reportUuid1: cacheObject, reportUuid2:cacheObject}
     *
     * @return
     */
    public Object getTplConcurrentTplCache(String cacheKey) {
        List<Object> oList = redisTemplate.boundHashOps(cacheKey).values();
        if (CollectionUtils.isNotEmpty(oList) && oList.get(0) != null) {
            return oList.get(0);
        }
        return null;
    }

    /**
     * 判断是否已有缓存
     *
     * @param cacheKey
     * @param reportUuid
     * @return
     */
    public boolean hasTplConcurrentTplCache(String cacheKey, String reportUuid) {
        return redisTemplate.boundHashOps(cacheKey).hasKey(reportUuid);
    }

    /**
     * 设置模板并发缓存数据
     *
     * @return
     */
    public void setTplConcurrentTplCache(String cacheKey, String reportUuid, Object cacheObj) {
        redisTemplate.boundHashOps(cacheKey).put(reportUuid, cacheObj);
        redisTemplate.expire(cacheKey, WinTestNGConstant.TPL_CACHE_LOCK_TIMEOUT, TimeUnit.SECONDS);
    }

    /**
     * 设置质量分样例数据
     *
     * @return
     */
    public void setQualityScoreNormalDistributionDTO(String cacheKey, NormalDistributionDTO dto) {
        redisTemplate.boundValueOps(cacheKey).set(dto, WinTestNGConstant.QUALITY_SCORE_DATA_CACHE_LIFE_TIME, TimeUnit.SECONDS);
    }

    /**
     * 查询质量分样例数据
     *
     * @return
     */
    public NormalDistributionDTO getQualityScoreNormalDistributionDTO(String cacheKey) {
        return (NormalDistributionDTO) redisTemplate.boundValueOps(cacheKey).get();
    }

}

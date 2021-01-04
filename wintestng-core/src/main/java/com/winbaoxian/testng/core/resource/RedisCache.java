package com.winbaoxian.testng.core.resource;

import com.winbaoxian.testng.enums.RedisSupportCommandType;
import com.winbaoxian.testng.model.core.resource.RedisCommandDTO;
import com.winbaoxian.testng.model.core.resource.RedisResourceSettings;
import com.winbaoxian.common.freemarker.utils.JsonUtils;
import com.winbaoxian.testng.utils.RedisShellUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;
import redis.clients.jedis.*;
import redis.clients.util.SafeEncoder;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2019-03-01 15:46
 */

public class RedisCache extends AbstractResourceCache<JedisPool> implements INoSqlOperation {

    private Logger log = LoggerFactory.getLogger(getClass());

    private JedisPool jedisPool;

    public RedisCache(RedisResourceSettings settings) {
        GenericObjectPoolConfig jedisPoolConfig = new GenericObjectPoolConfig();
        jedisPoolConfig.setMinIdle(ResourceConfigConstant.REDIS_POOL_MIN_SIZE);
        jedisPoolConfig.setMaxIdle(ResourceConfigConstant.REDIS_POOL_MAX_SIZE);
        jedisPoolConfig.setMaxTotal(ResourceConfigConstant.REDIS_POOL_MAX_SIZE);
        jedisPoolConfig.setTestWhileIdle(ResourceConfigConstant.REDIS_POOL_TEST_WHILE_IDLE);
        jedisPoolConfig.setTimeBetweenEvictionRunsMillis(ResourceConfigConstant.REDIS_TIME_BETWEEN_EVICTION_RUNS_MILLIS);
        jedisPoolConfig.setMinEvictableIdleTimeMillis(ResourceConfigConstant.REDIS_MIN_EVICTABLE_IDLE_TIME_MILLIS);
        JedisPool jedisPool = new JedisPool(jedisPoolConfig, settings.getHost(), settings.getPort(), Protocol.DEFAULT_TIMEOUT, settings.getPassword(), settings.getDatabase());
        this.jedisPool = jedisPool;
        this.settings = settings;
    }

    @Override
    public JedisPool getCacheObject() {
        return jedisPool;
    }

    @Override
    public Object execute(String command) {
        RedisCommandDTO commandDTO = RedisShellUtils.INSTANCE.parseShellCommand(command);
        log.info("parse RedisCommandDTO: {}", JsonUtils.INSTANCE.toJSONString(commandDTO));
        Object reply = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            Client client = jedis.getClient();
            Method method = ReflectionUtils.findMethod(Client.class, "sendCommand", Protocol.Command.class, String[].class);
            method.setAccessible(true);
            ReflectionUtils.invokeMethod(method, client, commandDTO.getCommandType().getCommand(), commandDTO.getArgs());
            //redis reply处理
            RedisSupportCommandType.RedisReplyType replyType = commandDTO.getCommandType().getReplyType();
            if (RedisSupportCommandType.RedisReplyType.STATUS_CODE.equals(replyType)) {
                reply = client.getStatusCodeReply();
            } else if (RedisSupportCommandType.RedisReplyType.BULK.equals(replyType)) {
                reply = client.getBulkReply();
            } else if (RedisSupportCommandType.RedisReplyType.MULTI_BULK.equals(replyType)) {
                reply = client.getMultiBulkReply();
            } else if (RedisSupportCommandType.RedisReplyType.INTEGER.equals(replyType)) {
                reply = client.getIntegerReply();
            } else if (RedisSupportCommandType.RedisReplyType.MULTI_INTEGER.equals(replyType)) {
                reply = client.getIntegerMultiBulkReply();
            } else if (RedisSupportCommandType.RedisReplyType.DOUBLE.equals(replyType)) {
                reply = BuilderFactory.DOUBLE.build(client.getOne());
            } else if (RedisSupportCommandType.RedisReplyType.STRING_SET.equals(replyType)) {
                reply = BuilderFactory.STRING_SET.build(client.getBinaryMultiBulkReply());
            } else if (RedisSupportCommandType.RedisReplyType.STRING_MAP.equals(replyType)) {
                reply = BuilderFactory.STRING_MAP.build(client.getBinaryMultiBulkReply());
            } else if (RedisSupportCommandType.RedisReplyType.OBJECT.equals(replyType)) {
                reply = evalResult(client.getOne());
            } else {
                reply = evalResult(client.getOne());
            }
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return reply;
    }

    private Object evalResult(Object result) {
        if (result instanceof byte[]) {
            return SafeEncoder.encode((byte[]) result);
        } else if (result instanceof List<?>) {
            List<?> list = (List<?>) result;
            List<Object> listResult = new ArrayList<>(list.size());
            for (Object bin : list) {
                listResult.add(evalResult(bin));
            }
            return listResult;
        }
        return result;
    }
}

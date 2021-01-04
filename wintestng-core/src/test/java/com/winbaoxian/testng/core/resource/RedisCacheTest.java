package com.winbaoxian.testng.core.resource;

import com.winbaoxian.common.freemarker.utils.JsonUtils;
import com.winbaoxian.testng.BaseTest;
import com.winbaoxian.testng.model.core.resource.RedisResourceSettings;
import org.testng.annotations.Test;

/**
 * @author dongxuanliang252
 * @date 2019-03-14 18:42
 */
public class RedisCacheTest extends BaseTest {

    @Test
    public void testExec() {
        RedisResourceSettings settings = new RedisResourceSettings();
        settings.setHost("proxy.winbaoxian.cn");
        settings.setPort(3736);
        settings.setDatabase(10);
        settings.setPassword("D6eAtdgwdzSdtUBz3xFQ5bFCysaisf");
        RedisCache redisCache = new RedisCache(settings);

        String[] commandArray = new String[]{
                " ping  ",
                " info  ",
                "set a 1",
                "get a",
                "exists a",
                "  hgetall  dong  ",
                "  eval \"return {KEYS[1],KEYS[2],ARGV[1],ARGV[2]}\" 2 key1 key2 first second",
                "  SCRIPT LOAD \"return 'hello moto'\" ",
                "EVALSHA \"232fd51614574cf0867b83d384a5e898cfd24e5a\" 0",
                "SCRIPT EXISTS 232fd51614574cf0867b83d384a5e898cfd24e5a",
                "  slowlog get 2  ",
                "slowlog len",
                "scan 0 count 30",
                "GEOADD Sicily 13.361389 38.115556 \"Palermo\" 15.087269 37.502669 \"Catania\""
        };
        for (String command : commandArray) {
            try {
                System.out.println(JsonUtils.INSTANCE.toJSONString(redisCache.execute(command)));
            } catch (Exception e) {
                log.error("error command:{}", command, e);
            }
        }
    }

}

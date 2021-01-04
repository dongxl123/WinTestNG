package com.winbaoxian.testng.core.resource;

/**
 * @author dongxuanliang252
 * @date 2019-03-01 16:51
 */
public interface ResourceConfigConstant {

    int MYSQL_POOL_MIN_SIZE = 0;
    int MYSQL_POOL_MAX_SIZE = 15;
    boolean MYSQL_TEST_WHILE_IDLE = true;
    String MYSQL_VALIDATION_QUERY = "SELECT 1";
    int MYSQL_TIME_BETWEEN_EVICTION_RUNS_MILLIS = 10 * 1000;
    int MYSQL_MIN_EVICTABLE_IDLE_TIME_MILLIS = 60 * 1000;
    boolean MYSQL_TEST_ON_BORROW = true;

    int MONGO_POOL_MIN_SIZE = 1;
    int MONGO_POOL_MAX_SIZE = 5;

    int REDIS_POOL_MIN_SIZE = 0;
    int REDIS_POOL_MAX_SIZE = 8;
    boolean REDIS_POOL_TEST_WHILE_IDLE = true;
    int REDIS_TIME_BETWEEN_EVICTION_RUNS_MILLIS = 10 * 1000;
    int REDIS_MIN_EVICTABLE_IDLE_TIME_MILLIS = 60 * 1000;

}

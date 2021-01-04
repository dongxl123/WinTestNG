package com.winbaoxian.testng.enums;

import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Protocol;

/**
 * @author dongxuanliang252
 * @date 2019-03-12 11:04
 */

public enum RedisSupportCommandType {
    PING(Protocol.Command.PING, RedisReplyType.STATUS_CODE),
    SET(Protocol.Command.SET, RedisReplyType.STATUS_CODE),
    GET(Protocol.Command.GET, RedisReplyType.BULK),
    QUIT(Protocol.Command.QUIT, RedisReplyType.STATUS_CODE),
    EXISTS(Protocol.Command.EXISTS, RedisReplyType.INTEGER),
    DEL(Protocol.Command.DEL, RedisReplyType.INTEGER),
    TYPE(Protocol.Command.TYPE, RedisReplyType.STATUS_CODE),
    FLUSHDB(Protocol.Command.FLUSHDB, RedisReplyType.STATUS_CODE),
    KEYS(Protocol.Command.KEYS, RedisReplyType.STRING_SET),
    RANDOMKEY(Protocol.Command.RANDOMKEY, RedisReplyType.BULK),
    RENAME(Protocol.Command.RENAME, RedisReplyType.STATUS_CODE),
    RENAMENX(Protocol.Command.RENAMENX, RedisReplyType.INTEGER),
    RENAMEX(Protocol.Command.RENAMEX, RedisReplyType.INTEGER),
    DBSIZE(Protocol.Command.DBSIZE, RedisReplyType.INTEGER),
    EXPIRE(Protocol.Command.EXPIRE, RedisReplyType.INTEGER),
    EXPIREAT(Protocol.Command.EXPIREAT, RedisReplyType.INTEGER),
    TTL(Protocol.Command.TTL, RedisReplyType.INTEGER),
    SELECT(Protocol.Command.SELECT, RedisReplyType.INTEGER),
    MOVE(Protocol.Command.MOVE, RedisReplyType.STATUS_CODE),
    FLUSHALL(Protocol.Command.FLUSHALL, RedisReplyType.STATUS_CODE),
    GETSET(Protocol.Command.GETSET, RedisReplyType.MULTI_BULK),
    MGET(Protocol.Command.MGET, RedisReplyType.MULTI_BULK),
    SETNX(Protocol.Command.SETNX, RedisReplyType.INTEGER),
    SETEX(Protocol.Command.SETEX, RedisReplyType.STATUS_CODE),
    MSET(Protocol.Command.MSET, RedisReplyType.STATUS_CODE),
    MSETNX(Protocol.Command.MSETNX, RedisReplyType.INTEGER),
    DECRBY(Protocol.Command.DECRBY, RedisReplyType.INTEGER),
    DECR(Protocol.Command.DECR, RedisReplyType.INTEGER),
    INCRBY(Protocol.Command.INCRBY, RedisReplyType.INTEGER),
    INCR(Protocol.Command.INCR, RedisReplyType.INTEGER),
    APPEND(Protocol.Command.APPEND, RedisReplyType.INTEGER),
    SUBSTR(Protocol.Command.SUBSTR, RedisReplyType.BULK),
    HSET(Protocol.Command.HSET, RedisReplyType.INTEGER),
    HGET(Protocol.Command.HGET, RedisReplyType.BULK),
    HSETNX(Protocol.Command.HSETNX, RedisReplyType.INTEGER),
    HMSET(Protocol.Command.HMSET, RedisReplyType.STATUS_CODE),
    HMGET(Protocol.Command.HMGET, RedisReplyType.MULTI_BULK),
    HINCRBY(Protocol.Command.HINCRBY, RedisReplyType.INTEGER),
    HEXISTS(Protocol.Command.HEXISTS, RedisReplyType.INTEGER),
    HDEL(Protocol.Command.HDEL, RedisReplyType.INTEGER),
    HLEN(Protocol.Command.HLEN, RedisReplyType.INTEGER),
    HKEYS(Protocol.Command.HKEYS, RedisReplyType.STRING_SET),
    HVALS(Protocol.Command.HVALS, RedisReplyType.MULTI_BULK),
    HGETALL(Protocol.Command.HGETALL, RedisReplyType.STRING_MAP),
    RPUSH(Protocol.Command.RPUSH, RedisReplyType.INTEGER),
    LPUSH(Protocol.Command.LPUSH, RedisReplyType.INTEGER),
    LLEN(Protocol.Command.LLEN, RedisReplyType.INTEGER),
    LRANGE(Protocol.Command.LRANGE, RedisReplyType.MULTI_BULK),
    LTRIM(Protocol.Command.LTRIM, RedisReplyType.STATUS_CODE),
    LINDEX(Protocol.Command.LINDEX, RedisReplyType.BULK),
    LSET(Protocol.Command.LSET, RedisReplyType.STATUS_CODE),
    LREM(Protocol.Command.LREM, RedisReplyType.INTEGER),
    LPOP(Protocol.Command.LPOP, RedisReplyType.BULK),
    RPOP(Protocol.Command.RPOP, RedisReplyType.BULK),
    RPOPLPUSH(Protocol.Command.RPOPLPUSH, RedisReplyType.BULK),
    SADD(Protocol.Command.SADD, RedisReplyType.INTEGER),
    SMEMBERS(Protocol.Command.SMEMBERS, RedisReplyType.STRING_SET),
    SREM(Protocol.Command.SREM, RedisReplyType.INTEGER),
    SPOP(Protocol.Command.SPOP, RedisReplyType.BULK),
    SMOVE(Protocol.Command.SMOVE, RedisReplyType.INTEGER),
    SCARD(Protocol.Command.SCARD, RedisReplyType.INTEGER),
    SISMEMBER(Protocol.Command.SISMEMBER, RedisReplyType.INTEGER),
    SINTER(Protocol.Command.SINTER, RedisReplyType.STRING_SET),
    SINTERSTORE(Protocol.Command.SINTERSTORE, RedisReplyType.INTEGER),
    SUNION(Protocol.Command.SUNION, RedisReplyType.STRING_SET),
    SUNIONSTORE(Protocol.Command.SUNIONSTORE, RedisReplyType.INTEGER),
    SDIFF(Protocol.Command.SDIFF, RedisReplyType.STRING_SET),
    SDIFFSTORE(Protocol.Command.SDIFFSTORE, RedisReplyType.INTEGER),
    SRANDMEMBER(Protocol.Command.SRANDMEMBER, RedisReplyType.BULK),
    ZADD(Protocol.Command.ZADD, RedisReplyType.INTEGER),
    ZRANGE(Protocol.Command.ZRANGE, RedisReplyType.STRING_SET),
    ZREM(Protocol.Command.ZREM, RedisReplyType.INTEGER),
    ZINCRBY(Protocol.Command.ZINCRBY, RedisReplyType.DOUBLE),
    ZRANK(Protocol.Command.ZRANK, RedisReplyType.INTEGER),
    ZREVRANK(Protocol.Command.ZREVRANK, RedisReplyType.INTEGER),
    ZREVRANGE(Protocol.Command.ZREVRANGE, RedisReplyType.STRING_SET),
    ZCARD(Protocol.Command.ZCARD, RedisReplyType.INTEGER),
    ZSCORE(Protocol.Command.ZSCORE, RedisReplyType.DOUBLE),
    MULTI(Protocol.Command.MULTI, RedisReplyType.NOT_SUPPORTED),
    DISCARD(Protocol.Command.DISCARD, RedisReplyType.NOT_SUPPORTED),
    EXEC(Protocol.Command.EXEC, RedisReplyType.NOT_SUPPORTED),
    WATCH(Protocol.Command.WATCH, RedisReplyType.NOT_SUPPORTED),
    UNWATCH(Protocol.Command.UNWATCH, RedisReplyType.NOT_SUPPORTED),
    SORT(Protocol.Command.SORT, RedisReplyType.MULTI_BULK),
    BLPOP(Protocol.Command.BLPOP, RedisReplyType.MULTI_BULK),
    BRPOP(Protocol.Command.BRPOP, RedisReplyType.MULTI_BULK),
    AUTH(Protocol.Command.AUTH, RedisReplyType.STATUS_CODE),
    SUBSCRIBE(Protocol.Command.SUBSCRIBE, RedisReplyType.NOT_SUPPORTED),
    PUBLISH(Protocol.Command.PUBLISH, RedisReplyType.NOT_SUPPORTED),
    UNSUBSCRIBE(Protocol.Command.UNSUBSCRIBE, RedisReplyType.NOT_SUPPORTED),
    PSUBSCRIBE(Protocol.Command.PSUBSCRIBE, RedisReplyType.NOT_SUPPORTED),
    PUNSUBSCRIBE(Protocol.Command.PUNSUBSCRIBE, RedisReplyType.NOT_SUPPORTED),
    PUBSUB(Protocol.Command.PUBSUB, RedisReplyType.NOT_SUPPORTED),
    ZCOUNT(Protocol.Command.ZCOUNT, RedisReplyType.INTEGER),
    ZRANGEBYSCORE(Protocol.Command.ZRANGEBYSCORE, RedisReplyType.STRING_SET),
    ZREVRANGEBYSCORE(Protocol.Command.ZREVRANGEBYSCORE, RedisReplyType.STRING_SET),
    ZREMRANGEBYRANK(Protocol.Command.ZREMRANGEBYRANK, RedisReplyType.INTEGER),
    ZREMRANGEBYSCORE(Protocol.Command.ZREMRANGEBYSCORE, RedisReplyType.INTEGER),
    ZUNIONSTORE(Protocol.Command.ZUNIONSTORE, RedisReplyType.INTEGER),
    ZINTERSTORE(Protocol.Command.ZINTERSTORE, RedisReplyType.INTEGER),
    ZLEXCOUNT(Protocol.Command.ZLEXCOUNT, RedisReplyType.INTEGER),
    ZRANGEBYLEX(Protocol.Command.ZRANGEBYLEX, RedisReplyType.STRING_SET),
    ZREVRANGEBYLEX(Protocol.Command.ZREVRANGEBYLEX, RedisReplyType.STRING_SET),
    ZREMRANGEBYLEX(Protocol.Command.ZREMRANGEBYLEX, RedisReplyType.INTEGER),
    SAVE(Protocol.Command.SAVE, RedisReplyType.NOT_SUPPORTED),
    BGSAVE(Protocol.Command.BGSAVE, RedisReplyType.NOT_SUPPORTED),
    BGREWRITEAOF(Protocol.Command.BGREWRITEAOF, RedisReplyType.NOT_SUPPORTED),
    LASTSAVE(Protocol.Command.LASTSAVE, RedisReplyType.NOT_SUPPORTED),
    SHUTDOWN(Protocol.Command.SHUTDOWN, RedisReplyType.NOT_SUPPORTED),
    INFO(Protocol.Command.INFO, RedisReplyType.BULK),
    MONITOR(Protocol.Command.MONITOR, RedisReplyType.NOT_SUPPORTED),
    SLAVEOF(Protocol.Command.SLAVEOF, RedisReplyType.NOT_SUPPORTED),
    CONFIG(Protocol.Command.CONFIG, RedisReplyType.MULTI_BULK),
    STRLEN(Protocol.Command.STRLEN, RedisReplyType.INTEGER),
    SYNC(Protocol.Command.SYNC, RedisReplyType.NOT_SUPPORTED),
    LPUSHX(Protocol.Command.LPUSHX, RedisReplyType.INTEGER),
    PERSIST(Protocol.Command.PERSIST, RedisReplyType.INTEGER),
    RPUSHX(Protocol.Command.RPUSHX, RedisReplyType.INTEGER),
    ECHO(Protocol.Command.ECHO, RedisReplyType.BULK),
    LINSERT(Protocol.Command.LINSERT, RedisReplyType.INTEGER),
    DEBUG(Protocol.Command.DEBUG, RedisReplyType.STATUS_CODE),
    BRPOPLPUSH(Protocol.Command.BRPOPLPUSH, RedisReplyType.BULK),
    SETBIT(Protocol.Command.SETBIT, RedisReplyType.INTEGER),
    GETBIT(Protocol.Command.GETBIT, RedisReplyType.INTEGER),
    BITPOS(Protocol.Command.BITPOS, RedisReplyType.INTEGER),
    SETRANGE(Protocol.Command.SETRANGE, RedisReplyType.INTEGER),
    GETRANGE(Protocol.Command.GETRANGE, RedisReplyType.BULK),
    EVAL(Protocol.Command.EVAL, RedisReplyType.OBJECT),
    EVALSHA(Protocol.Command.EVALSHA, RedisReplyType.OBJECT),
    SCRIPT(Protocol.Command.SCRIPT, RedisReplyType.OBJECT),
    SLOWLOG(Protocol.Command.SLOWLOG, RedisReplyType.OBJECT),
    OBJECT(Protocol.Command.OBJECT, RedisReplyType.OBJECT),
    BITCOUNT(Protocol.Command.BITCOUNT, RedisReplyType.INTEGER),
    BITOP(Protocol.Command.BITOP, RedisReplyType.INTEGER),
    SENTINEL(Protocol.Command.SENTINEL, RedisReplyType.NOT_SUPPORTED),
    DUMP(Protocol.Command.DUMP, RedisReplyType.NOT_SUPPORTED),
    RESTORE(Protocol.Command.RESTORE, RedisReplyType.NOT_SUPPORTED),
    PEXPIRE(Protocol.Command.PEXPIRE, RedisReplyType.INTEGER),
    PEXPIREAT(Protocol.Command.PEXPIREAT, RedisReplyType.INTEGER),
    PTTL(Protocol.Command.PTTL, RedisReplyType.INTEGER),
    INCRBYFLOAT(Protocol.Command.INCRBYFLOAT, RedisReplyType.DOUBLE),
    PSETEX(Protocol.Command.PSETEX, RedisReplyType.STATUS_CODE),
    CLIENT(Protocol.Command.CLIENT, RedisReplyType.NOT_SUPPORTED),
    TIME(Protocol.Command.TIME, RedisReplyType.MULTI_BULK),
    MIGRATE(Protocol.Command.MIGRATE, RedisReplyType.STATUS_CODE),
    HINCRBYFLOAT(Protocol.Command.HINCRBYFLOAT, null),
    SCAN(Protocol.Command.SCAN, RedisReplyType.OBJECT),
    HSCAN(Protocol.Command.HSCAN, RedisReplyType.OBJECT),
    SSCAN(Protocol.Command.SSCAN, RedisReplyType.OBJECT),
    ZSCAN(Protocol.Command.ZSCAN, RedisReplyType.OBJECT),
    WAIT(Protocol.Command.WAIT, RedisReplyType.NOT_SUPPORTED),
    CLUSTER(Protocol.Command.CLUSTER, RedisReplyType.NOT_SUPPORTED),
    ASKING(Protocol.Command.ASKING, RedisReplyType.STATUS_CODE),
    PFADD(Protocol.Command.PFADD, RedisReplyType.INTEGER),
    PFCOUNT(Protocol.Command.PFCOUNT, RedisReplyType.INTEGER),
    PFMERGE(Protocol.Command.PFMERGE, RedisReplyType.STATUS_CODE),
    READONLY(Protocol.Command.READONLY, RedisReplyType.NOT_SUPPORTED),
    GEOADD(Protocol.Command.GEOADD, RedisReplyType.INTEGER),
    GEODIST(Protocol.Command.GEODIST, RedisReplyType.DOUBLE),
    GEOHASH(Protocol.Command.GEOHASH, RedisReplyType.MULTI_BULK),
    GEOPOS(Protocol.Command.GEOPOS, RedisReplyType.OBJECT),
    GEORADIUS(Protocol.Command.GEORADIUS, RedisReplyType.OBJECT),
    GEORADIUSBYMEMBER(Protocol.Command.GEORADIUSBYMEMBER, RedisReplyType.OBJECT),
    BITFIELD(Protocol.Command.BITFIELD, RedisReplyType.MULTI_INTEGER),
    ;

    private Protocol.Command command;
    private RedisReplyType replyType;

    RedisSupportCommandType(Protocol.Command command, RedisReplyType replyType) {
        this.command = command;
        this.replyType = replyType;
    }

    public Protocol.Command getCommand() {
        return command;
    }

    public RedisReplyType getReplyType() {
        return replyType;
    }

    public static RedisSupportCommandType getByName(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        for (RedisSupportCommandType commandType : RedisSupportCommandType.values()) {
            if (commandType.command.name().equalsIgnoreCase(name)) {
                return commandType;
            }
        }
        return null;
    }

    public enum RedisReplyType {
        STATUS_CODE,
        BULK,
        MULTI_BULK,
        INTEGER,
        MULTI_INTEGER,
        DOUBLE,
        STRING_SET,
        STRING_MAP,
        OBJECT,
        NOT_SUPPORTED,
    }

}

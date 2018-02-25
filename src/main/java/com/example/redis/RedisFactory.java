package com.example.redis;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by Administrator on 2017/9/11/011.
 */
public class RedisFactory {

    private static JedisPool pool = null;
    private static final String REDIS_HOST="10.249.20.135";

    private static JedisPoolConfig config = null;
    public static JedisPool getInstance(){
        if (pool == null) {
            config = new JedisPoolConfig();
            config.setMaxIdle(3600);
            config.setMaxWaitMillis(10000);
            config.setMaxTotal(1024);
//            config.setMax
            config.setTestOnBorrow(true);
            config.setTestOnReturn(true);
//            pool = new JedisPool(config , "10.249.23.99" , 6379 , 30000);
            pool = new JedisPool(config , REDIS_HOST, 6379 , 30000);
        }
        return pool;
    }

    public static JedisPool getPool() {
        return pool;
    }

    public static void setPool (JedisPool jedisPool) {
        RedisFactory.pool = jedisPool;
    }

}

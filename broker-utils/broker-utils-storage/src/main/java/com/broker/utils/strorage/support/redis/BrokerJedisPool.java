package com.broker.utils.strorage.support.redis;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class BrokerJedisPool {
    private JedisPool pool = null;
    private RedisConfig redisConfig;
    public BrokerJedisPool(RedisConfig redisConfig){
        this.redisConfig = redisConfig;
        this.init();
    }

    public void init(){
        //创建jedis池配置实例
        JedisPoolConfig config = new JedisPoolConfig();
        if(redisConfig.getMaxActive() > 0){
            config.setMaxTotal(redisConfig.getMaxActive());
        }
        if(redisConfig.getMaxIdle() > 0){
            config.setMaxIdle(redisConfig.getMaxIdle());
        }
        if(redisConfig.getMaxWait() > 0){
            config.setMaxWaitMillis(redisConfig.getMaxWait());
        }
        this.pool = new JedisPool(config, redisConfig.getHost(), redisConfig.getPort());
    }

    public JedisPool getPool() {
        return pool;
    }
}

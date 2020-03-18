package com.broker.utils.strorage.support.redis;

import redis.clients.jedis.Jedis;

import java.util.HashSet;
import java.util.Set;

public class BrokerJedis {
    private BrokerJedisPool jedisPool = null;
    public BrokerJedis(RedisConfig config){
        this.jedisPool = new BrokerJedisPool(config);
    }

    private Jedis getJedis(){
        return this.jedisPool.getPool().getResource();
    }


    private void close(Jedis jedis){
        jedis.close();
    }

    /**
     * 获取hash表中所有key
     * @param name
     * @return
     */
    public Set<String> getHashAllKey(String name){
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.hkeys(name);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(jedis != null){
                close(jedis);
            }
        }
        return new HashSet<>();
    }

    /**
     * 从redis hash表中获取
     * @param hashName
     * @param key
     * @return
     */
    public String getHashKV(String hashName,String key){
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.hget(hashName, key);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(jedis != null){
                close(jedis);
            }
        }
        return null;
    }

    /**
     * 删除hash表的键值对
     * @param hashName
     * @param key
     */
    public Long delHashKV(String hashName,String key){
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.hdel(hashName,key);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(jedis != null){
                close(jedis);
            }
        }
        return null;
    }

    /**
     * 存放hash表键值对
     * @param hashName
     * @param key
     * @param value
     */
    public Long setHashKV(String hashName,String key,String value){
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.hset(hashName,key,value);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(jedis != null){
                close(jedis);
            }
        }
        return null;
    }

    /**
     * 删除键值对
     * @param k
     * @return
     */
    public Long delKV(String k){
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.del(k);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(jedis != null){
                close(jedis);
            }
        }
        return null;
    }

    /**
     * 放键值对
     * 永久
     * @param k
     * @param v
     */
    public String setKV(String k, String v)
    {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.set(k, v);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(jedis != null){
                close(jedis);
            }
        }
        return null;
    }


    /**
     * 放键值对
     *
     * @param k
     * @param v
     */
    public String setKV(String k,int second, String v)
    {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.setex(k,second, v);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(jedis != null){
                close(jedis);
            }
        }
        return null;
    }

    /**
     * 根据key取value
     *
     * @param k
     * @return
     */
    public String getKV(String k)
    {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.get(k);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(jedis != null){
                close(jedis);
            }
        }
        return null;
    }

}

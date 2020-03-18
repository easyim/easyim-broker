package com.broker.utils.strorage.support;

import com.broker.base.IStorage;
import com.broker.base.StorageException;
import com.broker.utils.strorage.support.redis.BrokerJedis;
import com.broker.utils.strorage.support.redis.RedisConfig;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author kong <androidsimu@163.com>
 * create by 2019/3/12 9:29
 * Description: easyimbroker
 **/
public class RedisStorage implements IStorage {
    private BrokerJedis brokerJedis = null;

    public RedisStorage(RedisConfig config){
        brokerJedis = new BrokerJedis(config);
    }

    @Override
    public void setKeyValue(String obj, String key, String value) throws StorageException {
        brokerJedis.setHashKV(obj, key, value);
    }

    @Override
    public void setKeyValue(String obj, Map<String, String> keyValues) throws StorageException {
        keyValues.forEach((k,v)->{
            brokerJedis.setHashKV(obj, k, v);
        });
    }

    @NotNull
    @Override
    public String getValue(String obj, String key) throws StorageException {
        String value =  brokerJedis.getHashKV(obj, key);
        return value == null ? "" : value;
    }

    @Override
    public Map<String, String> getValue(String obj, Set<String> keys) throws StorageException {
        Map<String, String> maps = new HashMap<>();
        keys.forEach(k->{
            String value = brokerJedis.getHashKV(obj, k);
            if(value != null){
                maps.put(k, value);
            }
        });
        return maps;
    }

    @Override
    public Map<String, String> getAllKeyValues(String obj) throws StorageException {
        Set<String> keys = brokerJedis.getHashAllKey(obj);
        Map<String, String> maps = new HashMap<>();
        keys.forEach(k->{
            String value = brokerJedis.getHashKV(obj, k);
            if(value != null){
                maps.put(k, brokerJedis.getHashKV(obj, k));
            }
        });
        return maps;
    }

    @Override
    public void remove(String obj, String key) throws StorageException {
        brokerJedis.delHashKV(obj, key);
    }
}

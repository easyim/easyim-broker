package com.broker.utils.strorage.support;

import com.broker.base.IStorage;
import com.broker.base.StorageException;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * @author kong <androidsimu@163.com>
 * create by 2019/3/12 9:29
 * Description: easyimbroker
 **/

@Slf4j
public class GoogleStorage implements IStorage {
//    Cache<String, V> cache = null;
    Map<String, Cache<String, String>> objCachesMap = new HashMap<>();
    public GoogleStorage(){
    }

    private void createObjCacheIfNotExist(String obj){
        if(!objCachesMap.containsKey(obj)){
            Cache<String, String> cache = CacheBuilder.newBuilder()
                    //设置cache的初始大小为10，要合理设置该值
                    .initialCapacity(10)
                    //设置并发数为5，即同一时间最多只能有5个线程往cache执行写入操作
                    .concurrencyLevel(5)
                    //构建cache实例
                    .build();
            objCachesMap.putIfAbsent(obj, cache);
        }
    }

    @Override
    public void setKeyValue(String obj, String key, String value) throws RuntimeException {
        createObjCacheIfNotExist(obj);
        Cache<String, String> objCache = objCachesMap.get(obj);
        if(objCache == null){
            throw new StorageException(" cache not exist.");
        }
        objCache.put(key, value);
    }

    @Override
    public void setKeyValue(String obj, Map<String, String> keyValues) throws RuntimeException {
        createObjCacheIfNotExist(obj);
        Cache<String, String> objCache = objCachesMap.get(obj);
        if(objCache == null){
            throw new StorageException(" cache not exist.");
        }
        keyValues.forEach((k,v)->{
            objCache.put(k, v);
        });

    }

//    @NotNull
    @NotNull
    public String getValue(String obj, String key) throws StorageException {
        createObjCacheIfNotExist(obj);
        Cache<String, String> objCache = objCachesMap.get(obj);
        if(objCache == null){
            throw new StorageException(" cache not exist.");
        }
        String value = null;
        value =  objCache.getIfPresent(key);
        if(value == null){
//            throw new StorageException("[storage] key:" + key + " not exist for:" + obj);
            log.info("[storage] key:" + key + " not exist for:" + obj);
            System.err.println("[storage] key:" + key + " not exist for:" + obj);
        }
        return value == null ? "" : value;
    }

    @Override
    public Map<String, String> getValue(String obj, Set<String> keys) throws StorageException {
        createObjCacheIfNotExist(obj);
        Cache<String, String> objCache = objCachesMap.get(obj);
        if(objCache == null){
            throw new StorageException(" cache not exist.");
        }
        Map<String, String> keyValues = new HashMap<>();
        List<String> errors = new ArrayList<>();
        keys.forEach(k->{
            String value =  objCache.getIfPresent(k);
            if(value == null){
                errors.add("[storage] key:" + k + " not exist for:" + obj);
            }
            else {
                keyValues.put(k, value);
            }
        });
        if(!errors.isEmpty()){
            System.err.println("waring:" + String.join("\n", errors));
        }
        return keyValues;
    }


    @Override
    public Map<String, String> getAllKeyValues(String obj) throws StorageException {
        createObjCacheIfNotExist(obj);
        Cache<String, String> objCache = objCachesMap.get(obj);
        if(objCache == null){
            throw new StorageException(" cache not exist.");
        }
        Map<String, String> maps = new HashMap<>();
        objCache.asMap().entrySet().forEach(v->{
            maps.put(v.getKey(), v.getValue());
        });
        return maps;
    }

    @Override
    public void remove(String obj, String key) throws StorageException {
        createObjCacheIfNotExist(obj);
        Cache<String, String> objCache = objCachesMap.get(obj);
        if(objCache == null){
            throw new StorageException(" cache not exist.");
        }
        objCache.invalidate(key);
    }
}

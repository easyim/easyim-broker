package com.broker.utils.strorage;

import com.broker.base.IStorage;
import com.broker.utils.strorage.support.GoogleStorage;
import com.broker.utils.strorage.support.RedisStorage;
import com.broker.utils.strorage.support.redis.RedisConfig;

/**
 * @author kong <androidsimu@163.com>
 * create by 2019/3/12 9:31
 * Description: easyimbroker
 **/
public class StorageFactory {
    public static final String STORAGE_TYPE_GOOGLE = "google";
    public static final String STORAGE_TYPE_REDIS = "redis";
    public static final String STORAGE_TYPE_MYSQL = "mysql";
    public static final String STORAGE_TYPE_MONGODB = "mongodb";

    private static IStorage storage = null;
    // 懒加载
    public  static  IStorage getInstance(){
        if(storage == null){
            storage =  StorageFactory.create();
            return storage;
        }
        else {
            return storage;
        }
    }
    public static   IStorage create(){
        // 默认使用 google cache
        String storageUsing = "".equals(System.getProperty("broker.storage.use", "")) ? StorageFactory.STORAGE_TYPE_GOOGLE : System.getProperty("broker.storage.use");
        if(StorageFactory.STORAGE_TYPE_GOOGLE.equals(storageUsing)){
            return new GoogleStorage();
        }
        else if(StorageFactory.STORAGE_TYPE_REDIS.equals(storageUsing)){
            //设置redis配置项值
            RedisConfig redisConfig = new RedisConfig();
            redisConfig.setHost(System.getProperty("broker.storage.redis.host", "localhost"));
            if(!"".equals(System.getProperty("broker.storage.redis.port", ""))){
                redisConfig.setPort(Integer.parseInt(System.getProperty("broker.storage.redis.port")));
            }
            if(!"".equals(System.getProperty("broker.storage.redis.maxidle", ""))){
                redisConfig.setMaxIdle(Integer.parseInt(System.getProperty("broker.storage.redis.maxidle")));
            }
            if(!"".equals(System.getProperty("broker.storage.redis.maxwait", ""))){
                redisConfig.setMaxWait(Integer.parseInt(System.getProperty("broker.storage.redis.maxwait")));
            }
            return new RedisStorage(redisConfig);
        }
        else if(StorageFactory.STORAGE_TYPE_MYSQL.equals(storageUsing)){
            return new GoogleStorage();
        }
        else if(StorageFactory.STORAGE_TYPE_MONGODB.equals(storageUsing)){
            return new GoogleStorage();
        }
        else {
            throw new RuntimeException(" broker storage initialization failed, with setting: broker.storage.use=" + System.getProperty("broker.storage.use"));
        }
    }
}

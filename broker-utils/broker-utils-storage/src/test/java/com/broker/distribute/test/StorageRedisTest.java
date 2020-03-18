package com.broker.distribute.test;

import com.broker.base.IStorage;
import com.broker.base.utils.ObjectUtils;
import com.broker.utils.strorage.StorageFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Map;

/**
 * @author kong <androidsimu@163.com>
 * create by 2019/3/12 10:20
 * Description: easyimbroker
 **/
public class StorageRedisTest {

    private IStorage storage = null;
    @Before
    public void init_storage(){
        System.setProperty("broker.storage.use", StorageFactory.STORAGE_TYPE_REDIS);
        System.setProperty("broker.storage.redis.host", "127.0.0.1");
        storage = StorageFactory.getInstance();
    }

    @Test
    public void _0_test_storage_google_ok(){
        storage.setKeyValue("users", "auid", "tom");
        storage.setKeyValue("users", "token", "123456");

        Map<String, String> values = storage.getValue("users", new HashSet<String>(){{
            add("auid");
            add("token");
        }});

        System.out.println(values.toString());
        String value = storage.getValue("users", "auid");
        System.out.println(value);
    }


    @Test
    public void _0_test_storage_get_all_kv(){
        storage.setKeyValue("users", "auid", "tom");
        storage.setKeyValue("users", "token", "123456");

        Map<String, String> allKeyValues = storage.getAllKeyValues("users");
        System.out.println("allKeyValues:" + ObjectUtils.json(allKeyValues));
    }
}

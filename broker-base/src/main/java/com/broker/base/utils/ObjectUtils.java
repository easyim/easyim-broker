package com.broker.base.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/**
 * @author kong <androidsimu@163.com>
 * create by 2019/3/12 16:14
 * Description: easyimbroker
 **/
public class ObjectUtils {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static boolean isNull(Object o) {
        return Objects.isNull(o);
    }

    public static boolean isNotNull(Object o) {
        return Objects.nonNull(o);
    }

    public static boolean strEmpty(String s) {
        return (null == s || "".equals(s));
    }

    public static boolean strNotEmpty(String s) {
        return !ObjectUtils.strEmpty(s);
    }


    /***
     *  对象转成json
     * @author kong
     * @date 2019/5/31 14:11
     * @param o: 源对象, targetClazz: 目标对象类型
     * @return json 字符串
     */
    public static String json(Object o) {
        try {
            return OBJECT_MAPPER.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "{}";
    }

    /**
     * 功能: 从json转成对象
     * @author kong
     * @date 2019/5/31 14:14
     * @param json: json字符串, targetClazz: 目标对象类型
     * @return obj 对象
     */

    @org.jetbrains.annotations.Nullable
    public static <T> T  beans(String json, Class<T> clazz){
        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /***
     *  拷贝一个对象
     *  @author kong
     * @date 2019/5/31 14:11
     * @param src: 源对象, targetClazz: 目标对象类型
     * @return 被复制的对象
     */
    @org.jetbrains.annotations.Nullable
    public static <T> T copy(Object src, Class<T> targetClazz){
        return ObjectUtils.beans(ObjectUtils.json(src), targetClazz);
    }

    /**
     * 功能: 从对象转成map
     * @author kong
     * @date 2019/5/31 14:14
     * @param o: 对象
     * @return map
     */
    public static Map<String, Object> maps(Object o) {
        try {
            return OBJECT_MAPPER.readValue(ObjectUtils.json(o),
                    new TypeReference<Map<String, Object>>() {
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyMap();
    }


}

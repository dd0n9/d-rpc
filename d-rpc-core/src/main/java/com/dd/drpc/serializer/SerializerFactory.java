package com.dd.drpc.serializer;

import com.fasterxml.jackson.databind.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * 序列化器工厂
 */
public class SerializerFactory {
    /**
     * 序列化器映射
     */
    private static final Map<String, Serializer> SERIALIZER_MAP = new HashMap<String, Serializer>() {
        {
            put(SerializerKeys.JDK, new JdkSerializer());
            put(SerializerKeys.JSON, new JSONSerializer());
            put(SerializerKeys.KRYO, new KryoSerializer());
            put(SerializerKeys.HESSIAN, new HessianSerializer());
        }
    };

    /**
     * 默认序列化器
     */
    private static final Serializer DEFAULT_SERIALIZER = SERIALIZER_MAP.get("jdk");

    public static Serializer getSerializer(String type) {
        return SERIALIZER_MAP.getOrDefault(type, DEFAULT_SERIALIZER);
    }
}

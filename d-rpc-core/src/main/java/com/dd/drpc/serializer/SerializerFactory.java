package com.dd.drpc.serializer;

import com.dd.drpc.spi.SpiLoader;
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
    static {
            SpiLoader.load(Serializer.class);
        }

    /**
     * 默认序列化器
     */
    private static final Serializer DEFAULT_SERIALIZER = new JdkSerializer();

    public static Serializer getSerializer(String type) {
        return SpiLoader.getInstance(Serializer.class, type);
    }
}

package com.dd.drpc.serializer;

/**
 * 序列化器接口
 */
public interface Serializer {

    /**
     * 序列化
     * @param obj
     * @return
     * @param <T>
     * @throws Exception
     */
    <T> byte[] serialize(T obj) throws Exception;

    /**
     * 反序列化
     * @param data
     * @param clazz
     * @return
     * @param <T>
     * @throws Exception
     */
    <T> T deserialize(byte[] data, Class<T> clazz) throws Exception;
}

package com.dd.drpc.serializer;

import com.dd.drpc.model.RpcRequest;
import com.dd.drpc.model.RpcResponse;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;

import java.io.IOException;

/**
 * JSON序列化器
 */
public class JSONSerializer implements Serializer {

    private static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        // 启用默认类型信息，用于处理接口、Object等动态类型
        BasicPolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator.builder()
                .allowIfSubType(Object.class)
                .build();

        objectMapper.activateDefaultTyping(ptv, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
    }

    @Override
    public <T> byte[] serialize(T obj) throws Exception {
        if (obj == null) {
            throw new IllegalArgumentException("Object to serialize cannot be null");
        }
        return objectMapper.writeValueAsBytes(obj);
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) throws Exception {
        if (data == null || data.length == 0) {
            throw new IllegalArgumentException("Data to deserialize cannot be null or empty");
        }
        return objectMapper.readValue(data, clazz);
    }

//    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
//
//    /**
//     * JSON序列化
//     * @param obj
//     * @return
//     * @param <T>
//     * @throws Exception
//     */
//    @Override
//    public <T> byte[] serialize(T obj) throws Exception {
//        if (obj instanceof RpcResponse) {
//            return handleRpcResponseSerialize((RpcResponse) obj);
//        }
//        if (obj instanceof RpcRequest) {
//            return handleRpcRequestSerialize((RpcRequest) obj);
//        }
//        return OBJECT_MAPPER.writeValueAsBytes(obj);
//    }
//
//    /**
//     * JSON反序列化
//     * @param data
//     * @param clazz
//     * @return
//     * @param <T>
//     * @throws Exception
//     */
//    @Override
//    public <T> T deserialize(byte[] data, Class<T> clazz) throws Exception {
//        T obj = OBJECT_MAPPER.readValue(data, clazz);
//        if (obj instanceof RpcResponse) {
//            return handleRpcResponseDeserialize((RpcResponse) obj, clazz);
//        }
//        if (obj instanceof RpcRequest) {
//            return handleRpcRequestDeserialize((RpcRequest) obj, clazz);
//        }
//        return obj;
//    }
//
//    /**
//     * 对Response序列化特殊处理
//     * @param response
//     * @return
//     * @throws IOException
//     */
//    private byte[] handleRpcResponseSerialize(RpcResponse response) throws IOException {
//        byte[] dataBytes = OBJECT_MAPPER.writeValueAsBytes(response.getData());
//        response.setData(dataBytes);
//        response.setDataTypes(response.getData().getClass());
//        return OBJECT_MAPPER.writeValueAsBytes(response);
//    }
//
//    /**
//     * 对Request序列化特殊处理
//     * @param request
//     * @return
//     * @throws IOException
//     */
//    private byte[] handleRpcRequestSerialize(RpcRequest request) throws IOException {
////        Object[] parameters = request.getParameters();
////        Class<?>[] parameterTypes = request.getParameterTypes();
////
////        for (int i = 0; i < parameters.length; i++) {
////            parameters[i] = OBJECT_MAPPER.writeValueAsBytes(parameters[i]);
////        }
////        request.setParameterTypes(parameterTypes);
////        request.setParameters(parameters);
//        return OBJECT_MAPPER.writeValueAsBytes(request);
//    }
//
//    /**
//     * 对Request反序列化特殊处理
//     * @param request
//     * @param clazz
//     * @return
//     * @param <T>
//     * @throws IOException
//     */
//    private <T> T handleRpcRequestDeserialize(RpcRequest request, Class<T> clazz) throws IOException {
//        Object[] parameters = request.getParameters();
//        Class<?>[] parameterTypes = request.getParameterTypes();
//
//        // 空参数直接返回
//        if (parameters == null || parameters.length == 0) {
//            return clazz.cast(request);
//        }
//
//        for (int i = 0; i < parameters.length; i++) {
//            Object param = parameters[i];
//            Class<?> targetType = parameterTypes[i];
//            if (param != null && !targetType.isInstance(param)) {
//                // 反序列化恢复类型
//                byte[] bytes = OBJECT_MAPPER.writeValueAsBytes(param);
//                parameters[i] = OBJECT_MAPPER.readValue(bytes, targetType);
//            }
//        }
//        return clazz.cast(request);
//    }
//
//    /**
//     * 对Response反序列化特殊处理
//     * @param response
//     * @param clazz
//     * @return
//     * @param <T>
//     * @throws IOException
//     */
//    private <T> T handleRpcResponseDeserialize(RpcResponse response, Class<T> clazz) throws IOException {
//        byte[] bytes = OBJECT_MAPPER.writeValueAsBytes(response.getData());
//        response.setData(OBJECT_MAPPER.readValue(bytes, response.getDataTypes()));
//        return clazz.cast(response);
//    }

}

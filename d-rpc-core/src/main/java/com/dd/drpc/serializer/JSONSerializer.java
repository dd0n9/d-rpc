package com.dd.drpc.serializer;

import com.dd.drpc.model.RpcRequest;
import com.dd.drpc.model.RpcResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * JSON序列化器
 */
public class JSONSerializer implements Serializer{
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * JSON序列化
     * @param obj
     * @return
     * @param <T>
     * @throws Exception
     */
    @Override
    public <T> byte[] serialize(T obj) throws Exception {
        return OBJECT_MAPPER.writeValueAsBytes(obj);
    }

    /**
     * JSON反序列化
     * @param data
     * @param clazz
     * @return
     * @param <T>
     * @throws Exception
     */
    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) throws Exception {
        T obj = OBJECT_MAPPER.readValue(data, clazz);
        if (obj instanceof RpcResponse) {
            return handleRpcResponse((RpcResponse) obj, clazz);
        }
        if (obj instanceof RpcRequest) {
            return handleRpcRequest((RpcRequest) obj, clazz);
        }
        return obj;
    }

    private <T> T handleRpcRequest(RpcRequest request, Class<T> clazz) throws IOException {
        Class<?>[] types = request.getParameterTypes();
        Object[] parameters = request.getParameters();

        for (int i = 0; i < types.length; i++) {
            Class<?> type = types[i];
            if (!type.isAssignableFrom(parameters[i].getClass())) {
                byte[] bytes = OBJECT_MAPPER.writeValueAsBytes(parameters[i]);
                parameters[i] = OBJECT_MAPPER.readValue(bytes, clazz);
            }
        }
        return clazz.cast(request);
    }

    /**
     * 对Response特殊处理
     * @param response
     * @param clazz
     * @return
     * @param <T>
     * @throws IOException
     */
    private <T> T handleRpcResponse(RpcResponse response, Class<T> clazz) throws IOException {
        byte[] bytes = OBJECT_MAPPER.writeValueAsBytes(response.getData());
        response.setData(OBJECT_MAPPER.readValue(bytes, response.getDataTypes()));
        return clazz.cast(response);
    }

}

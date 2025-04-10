package com.dd.drpc.server;

import com.dd.drpc.model.RpcRequest;
import com.dd.drpc.model.RpcResponse;
import com.dd.drpc.registry.LocalRegistry;
import com.dd.drpc.serializer.JdkSerializaer;
import com.dd.drpc.serializer.Serializer;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * Http请求处理
 */
public class HttpServerHandler implements Handler<HttpServerRequest> {
    @Override
    public void handle(HttpServerRequest httpServerRequest) {
        // 反序列化请求为对象，并从请求对象中获取参数。
        // 确定序列化器
        final Serializer serializer = new JdkSerializaer();
        //日志记录
        System.out.println("Recived request: " + httpServerRequest.method() + " " + httpServerRequest.uri());

        httpServerRequest.bodyHandler(body -> {
            byte[] bytes = body.getBytes();
            RpcRequest rpcRequest = null;
            try {
                rpcRequest = serializer.deserialize(bytes, RpcRequest.class);
            } catch (Exception e) {
                e.printStackTrace();
            }

            RpcResponse rpcResponse = new RpcResponse();
            if (rpcRequest == null) {
                rpcResponse.setMessage("rpcRequest is null");
                doResponse(httpServerRequest, rpcResponse, serializer);
                return;
            }
            //根据服务名称从本地注册器中获取到对应的服务实现类。
            Class<?> service = LocalRegistry.getService(rpcRequest.getServiceName());
            try {
                // 通过反射机制调用方法，得到返回结果。
                Method method = service.getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
                Object result = method.invoke(service.newInstance(), rpcRequest.getParameters());
                // 对返回结果进行封装和序列化，并写入到响应中。
                rpcResponse.setData(result);
                rpcResponse.setDataTypes(method.getReturnType());
                rpcResponse.setMessage("ok");
            } catch (Exception e) {
                e.printStackTrace();
                rpcResponse.setMessage(e.getMessage());
                rpcResponse.setException(e);
            }
            doResponse(httpServerRequest, rpcResponse, serializer);
        });

    }

    /**
     * 响应
     * @param httpServerRequest
     * @param rpcResponse
     * @param serializer
     */
    void doResponse(HttpServerRequest httpServerRequest, RpcResponse rpcResponse, Serializer serializer) {
        HttpServerResponse httpServerResponse = httpServerRequest.response()
                .putHeader("content-type", "application/json; charset=utf-8");
        try {
            byte[] serialized = serializer.serialize(rpcResponse);
            httpServerResponse.end(Buffer.buffer(serialized));
        } catch (Exception e) {
            e.printStackTrace();
            httpServerResponse.end(Buffer.buffer());
        }
    }
}


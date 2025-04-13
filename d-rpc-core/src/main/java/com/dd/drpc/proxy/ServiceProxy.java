package com.dd.drpc.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.dd.drpc.RpcApplication;
import com.dd.drpc.config.RpcConfig;
import com.dd.drpc.constant.RpcConstant;
import com.dd.drpc.model.RpcRequest;
import com.dd.drpc.model.RpcResponse;
import com.dd.drpc.model.ServiceMetaInfo;
import com.dd.drpc.registry.Registry;
import com.dd.drpc.registry.RegistryFactory;
import com.dd.drpc.serializer.Serializer;
import com.dd.drpc.serializer.SerializerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 服务代理 (动态代理)
 */
public class ServiceProxy implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Serializer serializer = SerializerFactory.getSerializer(RpcApplication.getRpcConfig().getSerializer());

        String serviceName = method.getDeclaringClass().getName();
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameters(args)
                .parameterTypes(method.getParameterTypes())
                .build();
        try {
            byte[] serialize = serializer.serialize(rpcRequest);

            RpcConfig rpcConfig = RpcApplication.getRpcConfig();
            Registry registry = RegistryFactory.getRegistry(rpcConfig.getRegistryConfig().getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceVersion(RpcConstant.DEAFAULT_SERVICE_VERSION);
            List<ServiceMetaInfo> serviceMetaInfos = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
            if (CollUtil.isEmpty(serviceMetaInfos)) {
                throw new RuntimeException("无服务地址");
            }
            ServiceMetaInfo metaInfo = serviceMetaInfos.get(0);

            try (HttpResponse httpResponse = HttpRequest.post(metaInfo.getServiceAddress())
                    .body(serialize)
                    .execute()) {
                byte[] result = httpResponse.bodyBytes();
                RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
                return rpcResponse.getData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

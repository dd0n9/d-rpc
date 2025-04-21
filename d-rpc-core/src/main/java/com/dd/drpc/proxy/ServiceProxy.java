package com.dd.drpc.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.dd.drpc.RpcApplication;
import com.dd.drpc.config.RpcConfig;
import com.dd.drpc.constant.RpcConstant;
import com.dd.drpc.loadbalancer.LoadBalancer;
import com.dd.drpc.loadbalancer.LoadBalancerFactory;
import com.dd.drpc.model.RpcRequest;
import com.dd.drpc.model.RpcResponse;
import com.dd.drpc.model.ServiceMetaInfo;
import com.dd.drpc.registry.Registry;
import com.dd.drpc.registry.RegistryFactory;
import com.dd.drpc.retry.RetryStrategy;
import com.dd.drpc.retry.RetryStrategyFactory;
import com.dd.drpc.serializer.Serializer;
import com.dd.drpc.serializer.SerializerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 服务代理 (动态代理)
 */
public class ServiceProxy implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 选择序列化器
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
            // 选择注册中心
            Registry registry = RegistryFactory.getRegistry(rpcConfig.getRegistryConfig().getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceVersion(RpcConstant.DEAFAULT_SERVICE_VERSION);
            serviceMetaInfo.setServiceHost(rpcConfig.getHost());
            serviceMetaInfo.setServicePort(rpcConfig.getPort());
            List<ServiceMetaInfo> serviceMetaInfos = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
            if (CollUtil.isEmpty(serviceMetaInfos)) {
                throw new RuntimeException("无服务地址");
            }

            // 负载均衡选用服务
            LoadBalancer loadBalancer = LoadBalancerFactory.getLoadBalancer(rpcConfig.getLoadbalancer());
            Map<String, Object> requestParams = new HashMap<>();
            requestParams.put("methodName", rpcRequest.getMethodName());
            ServiceMetaInfo selectedServiceMetaInfo = loadBalancer.select(requestParams, serviceMetaInfos);


            // 选择重试策略
            RetryStrategy retryStrategy = RetryStrategyFactory.getRetryStrategy(rpcConfig.getRetryStrategy());
            try (HttpResponse httpResponse = retryStrategy.doRetry(() ->
                HttpRequest.post(selectedServiceMetaInfo.getServiceAddress())
                        .body(serialize)
                        .execute()
            )) {
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

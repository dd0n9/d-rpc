package com.dd.example.provider;

import com.dd.drpc.RpcApplication;
import com.dd.drpc.config.RegistryConfig;
import com.dd.drpc.config.RpcConfig;
import com.dd.drpc.model.ServiceMetaInfo;
import com.dd.drpc.registry.LocalRegistry;
import com.dd.drpc.registry.Registry;
import com.dd.drpc.registry.RegistryFactory;
import com.dd.drpc.server.HttpServer;
import com.dd.drpc.server.VertxHttpServer;
import com.dd.example.common.service.UserService;

/**
 * 提供者启动类
 */
public class ProviderExample {
    public static void main(String[] args) {
        // 初始化
        RpcApplication.init();

        // 注册服务
        String serviceName = UserService.class.getName();
        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);
        System.out.println("正在注册服务到注册中心...");
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getRegistry(registryConfig.getRegistry());
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(serviceName);
        serviceMetaInfo.setServiceHost(rpcConfig.getHost());
        serviceMetaInfo.setServicePort(8088);
        serviceMetaInfo.setServiceVersion(rpcConfig.getVersion());
        try {
            registry.register(serviceMetaInfo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("注册服务到注册中心...");
        // 启动web服务
        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(RpcApplication.getRpcConfig().getPort());
    }
}

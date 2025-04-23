package com.dd.drpc.bootstrap;

import com.dd.drpc.RpcApplication;
import com.dd.drpc.config.RegistryConfig;
import com.dd.drpc.config.RpcConfig;
import com.dd.drpc.model.ServiceMetaInfo;
import com.dd.drpc.model.ServiceRegisterInfo;
import com.dd.drpc.registry.LocalRegistry;
import com.dd.drpc.registry.Registry;
import com.dd.drpc.registry.RegistryFactory;
import com.dd.drpc.server.HttpServer;
import com.dd.drpc.server.VertxHttpServer;

import java.util.List;

/**
 * 服务提供者初始化
 */
public class ProviderBootstrap {

    /**
     * 初始化
     */
    public static void init(List<ServiceRegisterInfo<?>> serviceRegisterInfoList) {
        // 初始化
        RpcApplication.init();

        // 注册服务
        for (ServiceRegisterInfo<?> serviceRegisterInfo : serviceRegisterInfoList) {
            String serviceName = serviceRegisterInfo.getServiceName();
            LocalRegistry.register(serviceRegisterInfo.getServiceName(), serviceRegisterInfo.getImplClass());
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

}

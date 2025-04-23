package com.dd.example.provider;

import com.dd.drpc.RpcApplication;
import com.dd.drpc.bootstrap.ProviderBootstrap;
import com.dd.drpc.config.RegistryConfig;
import com.dd.drpc.config.RpcConfig;
import com.dd.drpc.model.ServiceMetaInfo;
import com.dd.drpc.model.ServiceRegisterInfo;
import com.dd.drpc.registry.LocalRegistry;
import com.dd.drpc.registry.Registry;
import com.dd.drpc.registry.RegistryFactory;
import com.dd.drpc.server.HttpServer;
import com.dd.drpc.server.VertxHttpServer;
import com.dd.example.common.service.UserService;

import java.util.ArrayList;
import java.util.List;

/**
 * 提供者启动类
 */
public class ProviderExample {
    public static void main(String[] args) {

        // 选择要注册的服务
        List<ServiceRegisterInfo<?>> serviceRegisterInfoList = new ArrayList<>();
        ServiceRegisterInfo serviceRegisterInfo = new ServiceRegisterInfo(UserService.class.getName(), UserServiceImpl.class);
        serviceRegisterInfoList.add(serviceRegisterInfo);

        // 初始化提供者
        ProviderBootstrap.init(serviceRegisterInfoList);
    }
}

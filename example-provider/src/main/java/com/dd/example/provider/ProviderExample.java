package com.dd.example.provider;

import com.dd.drpc.RpcApplication;
import com.dd.drpc.registry.LocalRegistry;
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
        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);

        // 启动web服务
        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(RpcApplication.getRpcConfig().getPort());
    }
}

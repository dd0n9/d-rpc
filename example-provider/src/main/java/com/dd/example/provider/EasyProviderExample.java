package com.dd.example.provider;

import com.dd.drpc.registry.LocalRegistry;
import com.dd.drpc.server.HttpServer;
import com.dd.drpc.server.VertxHttpServer;
import com.dd.example.common.service.UserService;

/**
 * 简易提供者启动类
 */
public class EasyProviderExample {
    public static void main(String[] args) {
        // 注册服务到本地注册器
        LocalRegistry localRegistry = new LocalRegistry();
        localRegistry.register(UserService.class.getName(), UserServiceImpl.class);
        // 启动web服务
        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(8080);
    }
}

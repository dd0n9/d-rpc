package com.dd.example.provider;

import com.dd.drpc.server.HttpServer;
import com.dd.drpc.server.VertxHttpServer;

/**
 * 简易提供者启动类
 */
public class EasyProviderExample {
    public static void main(String[] args) {
        // 启动web服务
        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(8080);
    }
}

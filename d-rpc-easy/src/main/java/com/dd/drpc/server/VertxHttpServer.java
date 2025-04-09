package com.dd.drpc.server;

import io.vertx.core.Vertx;

public class VertxHttpServer implements HttpServer{


    @Override
    public void doStart(int port) {
        // 创建vertx实例
        Vertx vertx = Vertx.vertx();
        // 创建http服务器
        io.vertx.core.http.HttpServer httpServer = vertx.createHttpServer();

        // 处理请求
        httpServer.requestHandler(request -> {
            System.out.println("Received request:" + request.method() + " " + request.uri());

            request
                    .response()
                    .putHeader("content-type", "text/plain")
                    .end("Hello World From Vert.x");
        });

        httpServer.listen(port, result -> {
            if (result.succeeded()) {
                System.out.println("Server started on port: " + port);
            } else {
                System.out.println("Failed to start server on port: " + result.cause());
            }
        });
    }
}

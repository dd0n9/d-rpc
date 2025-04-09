package com.dd.drpc.server;

/**
 * Http服务器接口
 */
public interface HttpServer {
    /**
     * 开启服务
     * @param port
     */
    void doStart(int port);
}


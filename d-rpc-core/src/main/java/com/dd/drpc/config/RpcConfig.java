package com.dd.drpc.config;

import lombok.Data;

/**
 * RPC配置
 */
@Data
public class RpcConfig {

    /**
     * 名称
     */
    private String name = "drpc";

    /**
     * 版本
     */
    private String version = "1.0.0";

    /**
     * 主机
     */
    private String host = "localhost";

    /**
     * 端口号
     */
    private Integer port = 8080;
}

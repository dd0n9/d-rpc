package com.dd.drpc.config;

import com.dd.drpc.loadbalancer.LoadBalancerKeys;
import com.dd.drpc.serializer.SerializerKeys;
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

    /**
     * 模拟调用
     */
    private boolean mock = false;

    /**
     * 序列化器
     */
    private String serializer = SerializerKeys.JDK;

    /**
     * 注册中心
     */
    private RegistryConfig registryConfig = new RegistryConfig();

    /**
     * 负载均衡器
     */
    private String loadbalancer = LoadBalancerKeys.ROUND_ROBIN;
}

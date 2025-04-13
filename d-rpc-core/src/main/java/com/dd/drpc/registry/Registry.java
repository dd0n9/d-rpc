package com.dd.drpc.registry;

import com.dd.drpc.config.RegistryConfig;
import com.dd.drpc.model.ServiceMateInfo;

import java.util.List;

/**
 * 注册中心
 */
public interface Registry {

    /**
     * 初始化
     * @param registryConfig
     */
    void init(RegistryConfig registryConfig);

    /**
     * 服务提供端注册
     * @param serviceMateInfo
     */
    void register(ServiceMateInfo serviceMateInfo);

    /**
     * 注销服务
     * @param serviceMateInfo
     */
    void unregister(ServiceMateInfo serviceMateInfo);

    /**
     * 消费者端发现服务
     * @param servicekey
     * @return
     */
    List<ServiceMateInfo> serviceDiscovery(String servicekey);

    /**
     * 服务销毁
     */
    void destroy();
}

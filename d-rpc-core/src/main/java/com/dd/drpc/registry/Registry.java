package com.dd.drpc.registry;

import com.dd.drpc.config.RegistryConfig;
import com.dd.drpc.model.ServiceMetaInfo;

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
     * @param serviceMetaInfo
     */
    void register(ServiceMetaInfo serviceMetaInfo) throws Exception;

    /**
     * 注销服务
     * @param serviceMetaInfo
     */
    void unRegister(ServiceMetaInfo serviceMetaInfo);

    /**
     * 消费者端发现服务
     * @param servicekey
     * @return
     */
    List<ServiceMetaInfo> serviceDiscovery(String servicekey);

    /**
     * 服务销毁
     */
    void destroy();

    /**
     * 心跳检测
     */
    void heartBeat();

    /**
     * 服务器端监听
     * @param serviceNodeKey
     */
    void watch(String serviceNodeKey);
}

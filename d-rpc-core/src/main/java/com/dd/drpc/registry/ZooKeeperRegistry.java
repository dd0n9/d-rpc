package com.dd.drpc.registry;

import com.dd.drpc.config.RegistryConfig;
import com.dd.drpc.model.ServiceMetaInfo;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * ZooKeeper注册中心
 */
public class ZooKeeperRegistry implements Registry{

    /**
     * 注册服务缓存
     */
    private final RegistryServiceCache registryServiceCache = new RegistryServiceCache();

    /**
     * 本地注册节点集合
     */
    private final Set<String> localRegistedNodeKeysSet = new HashSet<>();

    /**
     * 根节点
     */
    private final String ZK_ROOT_PATH = "/drpc/zk";

    CuratorFramework client;

    ServiceDiscovery serviceDiscovery;

    /**
     * 初始化
     * @param registryConfig
     */
    @Override
    public void init(RegistryConfig registryConfig) {
        System.out.println("初始化ZooKeeper注册中心...");
        client = CuratorFrameworkFactory.builder()
                .connectString(registryConfig.getAddress())
                .retryPolicy(new ExponentialBackoffRetry(Math.toIntExact(registryConfig.getTimeout()), 3))
                .build();

        serviceDiscovery = ServiceDiscoveryBuilder.builder(ServiceMetaInfo.class)
                .client(client)
                .basePath(ZK_ROOT_PATH)
                .serializer(new JsonInstanceSerializer<>(ServiceMetaInfo.class))
                .build();

        try {
            client.start();
            serviceDiscovery.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 注册服务
     * @param serviceMetaInfo
     * @throws Exception
     */
    @Override
    public void register(ServiceMetaInfo serviceMetaInfo) throws Exception {
        serviceDiscovery.registerService(buildServiceInstance(serviceMetaInfo));

        // 加入本地注册节点集合
        String serviceKey = ZK_ROOT_PATH + "/" + serviceMetaInfo.getServiceKey();
        localRegistedNodeKeysSet.add(serviceKey);
    }

    /**
     * 注销服务
     * @param serviceMetaInfo
     */
    @Override
    public void unRegister(ServiceMetaInfo serviceMetaInfo) {
        try {
            serviceDiscovery.unregisterService(buildServiceInstance(serviceMetaInfo));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String serviceKey = ZK_ROOT_PATH + "/" + serviceMetaInfo.getServiceKey();
        localRegistedNodeKeysSet.remove(serviceKey);
    }

    /**
     * 服务发现
     * @param servicekey
     * @return
     */
    @Override
    public List<ServiceMetaInfo> serviceDiscovery(String servicekey) {
        List<ServiceMetaInfo> cachedServiceInfos = registryServiceCache.readCache(servicekey);
        if (cachedServiceInfos != null) {
            System.out.println("从缓存返回服务信息");
            return cachedServiceInfos;
        }

        try {
            Collection<ServiceInstance<ServiceMetaInfo>> serviceInstancesInfos = serviceDiscovery.queryForInstances(servicekey);
            List<ServiceMetaInfo> serviceMetaInfos = serviceInstancesInfos.stream()
                    .map(ServiceInstance::getPayload)
                    .collect(Collectors.toList());

            registryServiceCache.writeCache(servicekey, serviceMetaInfos);
            System.out.println("从本地返回服务信息");
            return serviceMetaInfos;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 服务销毁
     */
    @Override
    public void destroy() {
        System.out.println("节点下线");
        for (String key : localRegistedNodeKeysSet) {
            try {
                client.delete().guaranteed().forPath(key);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        if (client != null) {
            client.close();
        }

    }

    @Override
    public void heartBeat() {

    }

    @Override
    public void watch(String serviceNodeKey) {

    }

    public ServiceInstance<ServiceMetaInfo> buildServiceInstance(ServiceMetaInfo serviceMetaInfo) {
        String serviceAddress = serviceMetaInfo.getServiceHost() + ":" + serviceMetaInfo.getServicePort();
        try {
            return ServiceInstance.<ServiceMetaInfo>builder()
                    .id(serviceAddress)
                    .name(serviceMetaInfo.getServiceKey())
                    .address(serviceAddress)
                    .payload(serviceMetaInfo)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

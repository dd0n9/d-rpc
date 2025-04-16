package com.dd.drpc.registry;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import cn.hutool.json.JSONUtil;
import com.dd.drpc.config.RegistryConfig;
import com.dd.drpc.model.ServiceMetaInfo;
import io.etcd.jetcd.*;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.PutOption;
import io.etcd.jetcd.watch.WatchEvent;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Etcd注册中心
 */
public class EtcdRegistry implements Registry{

    /**
     * 注册节点集合
     */
    private final Set<String> localRegistedNodeKeysSet = new HashSet<>();

    /**
     * 注册服务缓存
     */
    private final RegistryServiceCache registryServiceCache = new RegistryServiceCache();

    /**
     * 正在监听的key
     */
    private final Set<String> watchingKeySet = new ConcurrentHashSet<>();

    Client client;
    KV kvClient;

    /**
     * 根节点
     */
    private static final String ETCD_ROOT_PATH = "/drpc/etcd";

    /**
     * 初始化
     * @param registryConfig
     */
    @Override
    public void init(RegistryConfig registryConfig) {
        client = Client.builder().endpoints(registryConfig.getAddress())
                .connectTimeout(Duration.ofMillis(registryConfig.getTimeout()))
                .build();
        kvClient = client.getKVClient();
        heartBeat();
    }

    /**
     * 注册服务
     * @param serviceMetaInfo
     * @throws Exception
     */
    @Override
    public void register(ServiceMetaInfo serviceMetaInfo) throws Exception {
        Lease leaseClient = client.getLeaseClient();

        // 租约
        long leaseId = leaseClient.grant(30).get().getID();

        // 设置键值对
        String registerKey = ETCD_ROOT_PATH + serviceMetaInfo.getServiceNodeKey();
        ByteSequence key = ByteSequence.from(registerKey, StandardCharsets.UTF_8);
        ByteSequence value = ByteSequence.from(JSONUtil.toJsonStr(serviceMetaInfo), StandardCharsets.UTF_8);

        // 键值对租约关联
        PutOption putOption = PutOption.builder().withLeaseId(leaseId).build();
        kvClient.put(key, value, putOption).get();
        localRegistedNodeKeysSet.add(registerKey);
    }

    /**
     * 注销服务
     * @param serviceMetaInfo
     */
    @Override
    public void unRegister(ServiceMetaInfo serviceMetaInfo) {
        String registerKey = ETCD_ROOT_PATH + serviceMetaInfo.getServiceNodeKey();
        ByteSequence key = ByteSequence.from(registerKey, StandardCharsets.UTF_8);
        kvClient.delete(key);
        localRegistedNodeKeysSet.remove(registerKey);
    }

    /**
     * 服务发现
     * @param servicekey
     * @return
     */
    @Override
    public List<ServiceMetaInfo> serviceDiscovery(String servicekey) {
        // 优先从缓存中获取服务
        List<ServiceMetaInfo> cachedServiceMetaInfos = registryServiceCache.readCache(servicekey);
        if (cachedServiceMetaInfos != null) {
            System.out.println("从缓存中获取服务信息....");
            return cachedServiceMetaInfos;
        }

        // 搜索前缀
        String searchPrefix = ETCD_ROOT_PATH + servicekey + "/";

        // 前缀查询
        GetOption getOption = GetOption.builder().isPrefix(true).build();
        try {
            List<KeyValue> keyValues = kvClient.get(
                            ByteSequence.from(searchPrefix, StandardCharsets.UTF_8),
                            getOption)
                    .get()
                    .getKvs();
            List<ServiceMetaInfo> serviceMetaInfos = keyValues.stream()
                    .map(keyValue -> {
                        String key = keyValue.getKey().toString(StandardCharsets.UTF_8);
                        watch(key);
                        String value = keyValue.getValue().toString(StandardCharsets.UTF_8);
                        return JSONUtil.toBean(value, ServiceMetaInfo.class);
                    })
                    .collect(Collectors.toList());
            // 存入缓存
            registryServiceCache.writeCache(servicekey, serviceMetaInfos);
            System.out.println("从注册中心获取服务信息...");
            return serviceMetaInfos;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 销毁服务
     */
    @Override
    public void destroy() {
        System.out.println("节点下线");
        for (String key : localRegistedNodeKeysSet) {
            try {
                kvClient.delete(ByteSequence.from(key, StandardCharsets.UTF_8)).get();
            }catch (Exception e) {
                throw new RuntimeException("节点下线失败", e);
            }
        }
        // 资源释放
        if (kvClient != null) {
            kvClient.close();
        }
        if (client != null) {
            client.close();
        }
    }

    /**
     * 心跳检测
     */
    @Override
    public void heartBeat() {
        CronUtil.schedule("*/10 * * * * *", new Task() {
            @Override
            public void execute() {
                for (String key : localRegistedNodeKeysSet) {
                    try {
                        List<KeyValue> keyValues = kvClient.get(ByteSequence.from(key, StandardCharsets.UTF_8))
                                .get()
                                .getKvs();
                        // 节点过期
                        if (CollUtil.isEmpty(keyValues)) {
                            continue;
                        }
                        // 没过期则续签
                        KeyValue keyValue = keyValues.get(0);
                        String value = keyValue.getValue().toString(StandardCharsets.UTF_8);
                        ServiceMetaInfo serviceMetaInfo = JSONUtil.toBean(value, ServiceMetaInfo.class);
                        register(serviceMetaInfo);
                    } catch (Exception e) {
                        throw new RuntimeException(key + "续签失败", e);
                    }
                }
            }
        });
        CronUtil.setMatchSecond(true);
        CronUtil.start();
    }

    /**
     * 监听
     * @param serviceNodeKey
     */
    @Override
    public void watch(String serviceNodeKey) {
        Watch watchClient = client.getWatchClient();
        boolean added = watchingKeySet.add(serviceNodeKey);
        if (added) {
            watchClient.watch(ByteSequence.from(serviceNodeKey, StandardCharsets.UTF_8), watchResponse -> {
                for (WatchEvent event : watchResponse.getEvents()) {
                    switch (event.getEventType()) {
                        case DELETE:
                            registryServiceCache.clearCache();
                            break;
                        case PUT:
                        default:
                            break;
                    }
                }
            });
        }

    }
}

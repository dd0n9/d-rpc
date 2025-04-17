package com.dd.drpc.loadbalancer;

import com.dd.drpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ConsistentHashLoadBalancer implements LoadBalancer {

    /**
     * 哈希环
     */
    private final TreeMap<Integer, ServiceMetaInfo> virtualNodes = new TreeMap<Integer, ServiceMetaInfo>();

    /**
     * 虚拟节点数
     */
    private static final int VIRTUAL_NODE_SIZE = 32;

    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParam, List<ServiceMetaInfo> serviceMetaInfoList) {
        System.out.println("ConsistentHashLoadBalancer select...");
        if (serviceMetaInfoList.isEmpty()) {
            return null;
        }

        // 构造哈希环
        for (ServiceMetaInfo serviceMetaInfo : serviceMetaInfoList) {
            for (int i = 0; i < VIRTUAL_NODE_SIZE; i++) {
                int hash = getHash(serviceMetaInfo.getServiceAddress() + "#" + i);
                virtualNodes.put(hash, serviceMetaInfo);
            }
        }

        int hash = getHash(requestParam);
        Map.Entry<Integer, ServiceMetaInfo> entry = virtualNodes.ceilingEntry(hash);
        // 如果没有大于请求hash的节点则返回第一个节点
        if (entry == null) {
            entry = virtualNodes.firstEntry();
        }
        return entry.getValue();

    }

    /**
     * 生成哈希值
     * @param object
     * @return
     */
    private int getHash(Object object) {
        String key = object.toString();
        int hash = 0xdeadbeef;
        int len = key.length();
        for (int i = 0; i < len; i++) {
            hash ^= key.charAt(i);
            hash *= 0x5bd1e995;
            hash ^= hash >>> 15;
        }
        return hash;
    }
}

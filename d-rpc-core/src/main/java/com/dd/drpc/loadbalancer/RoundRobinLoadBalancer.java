package com.dd.drpc.loadbalancer;

import com.dd.drpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 轮询负载均衡器
 */
public class RoundRobinLoadBalancer implements LoadBalancer {

    /**
     * 轮询下标
     */
    private final AtomicInteger currentIndex = new AtomicInteger(0);

    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParam, List<ServiceMetaInfo> serviceMetaInfoList) {
        System.out.println("RoundRobinLoadBalancer select...");
        if (serviceMetaInfoList.isEmpty()) {
            return null;
        }

        int size = serviceMetaInfoList.size();
        if (size == 1) {
            return serviceMetaInfoList.get(0);
        }

        int index = currentIndex.getAndIncrement() % size;
        ServiceMetaInfo serviceMetaInfo = serviceMetaInfoList.get(index);
        System.out.println("RoundRobinLoadBalancer selected serviceMetaInfo: " + serviceMetaInfo);
        return serviceMetaInfo;
    }
}

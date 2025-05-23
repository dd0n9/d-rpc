package com.dd.drpc.loadbalancer;

import com.dd.drpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 随机负载均衡器
 */
public class RandomLoadBalancer implements LoadBalancer {

    /**
     * 随机数
     */
    private final Random random = new Random();

    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParam, List<ServiceMetaInfo> serviceMetaInfoList) {
        System.out.println("RandomLoadBalancer select...");
        int size = serviceMetaInfoList.size();
        if (size == 0) {
            return null;
        }

        if (size == 1) {
            return serviceMetaInfoList.get(0);
        }
        ServiceMetaInfo serviceMetaInfo = serviceMetaInfoList.get(random.nextInt(size));
        System.out.println("selected serviceMetaInfo: " + serviceMetaInfo);
        return serviceMetaInfo;
    }
}

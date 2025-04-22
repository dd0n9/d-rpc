package com.dd.drpc.tolerant;

import com.dd.drpc.model.RpcResponse;

import java.util.Map;

/**
 * 快速失败容错机制
 */
public class FailFastTolerantStrategy implements TolerantStrategy{
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        System.out.println("运行快速失败容错机制");
        throw new RuntimeException("服务报错", e);
    }
}

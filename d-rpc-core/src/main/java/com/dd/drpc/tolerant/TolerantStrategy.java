package com.dd.drpc.tolerant;

import com.dd.drpc.model.RpcResponse;

import java.util.Map;

/**
 * 容错机制
 */
public interface TolerantStrategy {

    /**
     * 容错
     * @param context
     * @param e
     * @return
     */
    RpcResponse doTolerant(Map<String, Object> context, Exception e);

}

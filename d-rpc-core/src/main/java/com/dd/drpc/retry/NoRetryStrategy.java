package com.dd.drpc.retry;

import com.dd.drpc.model.RpcResponse;

import java.util.concurrent.Callable;

/**
 * 不重试策略
 */
public class NoRetryStrategy implements RetryStrategy {

    @Override
    public RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception {
        System.out.println("执行不重试策略...");
        return callable.call();
    }
}

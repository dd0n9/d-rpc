package com.dd.drpc.retry;

import cn.hutool.http.HttpResponse;
import com.dd.drpc.model.RpcResponse;
import com.google.api.Http;

import java.util.concurrent.Callable;

/**
 * 不重试策略
 */
public class NoRetryStrategy implements RetryStrategy {

    @Override
    public HttpResponse doRetry(Callable<HttpResponse> callable) throws Exception {
        System.out.println("执行不重试策略...");
        return callable.call();
    }
}

package com.dd.drpc.retry;

import cn.hutool.http.HttpResponse;
import com.dd.drpc.model.RpcRequest;
import com.dd.drpc.model.RpcResponse;

import java.util.concurrent.Callable;

/**
 * 重试策略
 */
public interface RetryStrategy {

    /**
     * 重试
     * @param callable
     * @return
     * @throws Exception
     */
    HttpResponse doRetry(Callable<HttpResponse> callable) throws Exception;
}

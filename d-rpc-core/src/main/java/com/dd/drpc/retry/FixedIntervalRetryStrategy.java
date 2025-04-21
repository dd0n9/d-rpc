package com.dd.drpc.retry;

import cn.hutool.http.HttpResponse;
import com.dd.drpc.model.RpcResponse;
import com.github.rholder.retry.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * 固定时间间隔重试
 */
@Slf4j
public class FixedIntervalRetryStrategy implements RetryStrategy {

    @Override
    public HttpResponse doRetry(Callable<HttpResponse> callable) throws Exception {
        System.out.println("执行固定时间间隔重试策略...");
        Retryer<HttpResponse> retryer = RetryerBuilder.<HttpResponse>newBuilder()
                .retryIfExceptionOfType(Exception.class)
                .withWaitStrategy(WaitStrategies.fixedWait(3L, TimeUnit.SECONDS))
                .withStopStrategy(StopStrategies.stopAfterAttempt(4))
                .withRetryListener(new RetryListener() {
                    @Override
                    public <V> void onRetry(Attempt<V> attempt) {
                        if (attempt.getAttemptNumber() == 1) {
                            log.info("正在进行第一次尝试...");
                        }
                        else {
                            log.info("正在重试...,重试次数{}...", attempt.getAttemptNumber()-1);
                        }
                    }
                }).build();
        return retryer.call(callable);
    }
}

package com.dd.drpc.retry;

import com.dd.drpc.model.RpcResponse;
import org.junit.Test;

/**
 * 重试策略测试
 */
public class RetryStrategyTest {

    RetryStrategy retryStrategy = new FixedIntervalRetryStrategy();

    @Test
    public void doRetry() {
        try {
            RpcResponse rpcResponse = retryStrategy.doRetry(() -> {
                System.out.println("重试策略");
                throw new RuntimeException("重试策略测试失败");
            });
            System.out.println(rpcResponse);
        } catch (Exception e) {
            System.out.println("重试失败");
            e.printStackTrace();
        }
    }

}

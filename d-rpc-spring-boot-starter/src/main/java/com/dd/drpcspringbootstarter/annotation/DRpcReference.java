package com.dd.drpcspringbootstarter.annotation;

import com.dd.drpc.constant.RpcConstant;
import com.dd.drpc.loadbalancer.LoadBalancerKeys;
import com.dd.drpc.retry.RetryStrategyKeys;
import com.dd.drpc.tolerant.TolerantStrategyKeys;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 服务消费者注解
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DRpcReference {

    /**
     * 服务实现类
     * @return
     */
    Class<?> interfaceClass() default void.class;

    /**
     * 服务版本
     * @return
     */
    String version() default RpcConstant.DEAFAULT_SERVICE_VERSION;

    /**
     * 负载均衡器
     * @return
     */
    String loadBalnacer() default LoadBalancerKeys.ROUND_ROBIN;

    /**
     * 重试策略
     * @return
     */
    String RetryStrategy() default RetryStrategyKeys.FIXED_INTERVAL;

    /**
     * 容错策略
     * @return
     */
    String TolerantStrategy() default TolerantStrategyKeys.FAIL_SAFE;

}


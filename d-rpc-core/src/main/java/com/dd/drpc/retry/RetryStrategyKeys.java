package com.dd.drpc.retry;

/**
 * 重试策略常量
 */
public interface RetryStrategyKeys {

    /**
     * 不重试
     */
    String NO = "no";

    /**
     * 固定时间间隔重试
     */
    String FIXED_INTERVAL = "fixedInterval";
}

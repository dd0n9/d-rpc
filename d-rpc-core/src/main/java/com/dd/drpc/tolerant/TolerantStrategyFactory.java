package com.dd.drpc.tolerant;

import com.dd.drpc.spi.SpiLoader;

public class TolerantStrategyFactory {

    static {
        SpiLoader.load(TolerantStrategy.class);
    }

    /**
     * 默认容错机制
     */
    private static final TolerantStrategy DEAFAULT_TOLERANT_STRATEGY = new FailFastTolerantStrategy();

    /**
     * 获取容错机制实例
     * @param key
     * @return
     */
    public static TolerantStrategy getTolerantStrategy(String key) {
        return SpiLoader.getInstance(TolerantStrategy.class, key);
    }
}

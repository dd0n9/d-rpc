package com.dd.drpc.registry;

import com.dd.drpc.spi.SpiLoader;

/**
 * 注册器工厂
 */
public class RegistryFactory {
    static {
        SpiLoader.load(Registry.class);
    }

    /**
     * 默认注册器
     */
    private static final Registry DEFAULT_REGISTRY = new EtcdRegistry();

    public static Registry getRegistry(String type) {
        return SpiLoader.getInstance(Registry.class, type);
    }
}

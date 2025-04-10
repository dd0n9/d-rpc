package com.dd.drpc;

import com.dd.drpc.config.RpcConfig;
import com.dd.drpc.constant.RpcConstant;
import com.dd.drpc.utils.ConfigUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * RPC启动类
 * 管理全局配置
 */
@Slf4j
public class RpcApplication {

    private static volatile RpcConfig rpcConfig;

    /**
     * 初始化，支持传入自定义配置
     * @param newrpcConfig
     */
    public static void init(RpcConfig newrpcConfig) {
        rpcConfig = newrpcConfig;
        log.info("Rpc init, config={}" + newrpcConfig.toString());
    }

    /**
     * 初始化
     */
    public static void init() {
        RpcConfig newRpcConfig;
        try {
            newRpcConfig = ConfigUtils.loadConfig(RpcConfig.class, RpcConstant.DEAFAULT_CONFIG_PREFIX);
        } catch (Exception e) {
            newRpcConfig = new RpcConfig();
        }
        init(newRpcConfig);
    }

    public static RpcConfig getRpcConfig() {
        if (rpcConfig == null) {
            synchronized (RpcApplication.class) {
                if (rpcConfig == null) {
                    init();
                }
            }
        }
        return rpcConfig;
    }


}

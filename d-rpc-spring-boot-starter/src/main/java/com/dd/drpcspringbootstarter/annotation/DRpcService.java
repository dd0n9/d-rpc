package com.dd.drpcspringbootstarter.annotation;

import com.dd.drpc.constant.RpcConstant;

/**
 * 服务提供者注解
 */
public @interface DRpcService {

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
}

package com.dd.drpcspringbootstarter.annotation;

/**
 * Rpc启动注解
 */
public @interface EnnableDRpc {

    /**
     * 需要服务
     * @return
     */
    boolean needServer() default true;
}

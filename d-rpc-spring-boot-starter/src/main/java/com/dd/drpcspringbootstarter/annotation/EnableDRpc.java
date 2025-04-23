package com.dd.drpcspringbootstarter.annotation;

import com.dd.drpcspringbootstarter.bootstrap.RpcConsumerBootstrap;
import com.dd.drpcspringbootstarter.bootstrap.RpcInitBootstrap;
import com.dd.drpcspringbootstarter.bootstrap.RpcProviderBootstrap;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Rpc启动注解
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({RpcInitBootstrap.class, RpcProviderBootstrap.class, RpcConsumerBootstrap.class})
public @interface EnableDRpc {

    /**
     * 需要服务
     * @return
     */
    boolean needServer() default true;
}

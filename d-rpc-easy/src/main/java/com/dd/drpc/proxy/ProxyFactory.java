package com.dd.drpc.proxy;

import java.lang.reflect.Proxy;

/**
 * 服务代理工厂
 */
public class ProxyFactory {

    public static <T> T getProxy(Class<T> serviceClass) {
        return (T) Proxy.newProxyInstance(
                serviceClass.getClassLoader(),
                new Class[] {serviceClass},
                new ServiceProxy()
        );
    }
 }

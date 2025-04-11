package com.dd.drpc.proxy;

import com.dd.drpc.RpcApplication;
import com.dd.drpc.config.RpcConfig;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * 服务代理工厂
 */
public class ProxyFactory {

    /**
     * 获取代理
     * @param serviceClass
     * @return
     * @param <T>
     */
    public static <T> T getProxy(Class<T> serviceClass) {
        if (RpcApplication.getRpcConfig().isMock()) {
            return getMockProxy(serviceClass);
        }
        return (T) Proxy.newProxyInstance(
                serviceClass.getClassLoader(),
                new Class[] {serviceClass},
                new ServiceProxy()
        );
    }

    /**
     * 获取Mock代理
     * @param serviceClass
     * @return
     * @param <T>
     */
    public static <T> T getMockProxy(Class<T> serviceClass) {
        return (T) Proxy.newProxyInstance(serviceClass.getClassLoader(),
                new Class[]{serviceClass},
                new MockServiceProxy()
        );
    }

 }

package com.dd.drpcspringbootstarter.bootstrap;

import com.dd.drpc.RpcApplication;
import com.dd.drpc.config.RegistryConfig;
import com.dd.drpc.config.RpcConfig;
import com.dd.drpc.model.ServiceMetaInfo;
import com.dd.drpc.registry.LocalRegistry;
import com.dd.drpc.registry.Registry;
import com.dd.drpc.registry.RegistryFactory;
import com.dd.drpcspringbootstarter.annotation.DRpcService;
import jdk.vm.ci.code.RegisterConfig;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * 服务提供者启动类
 */
public class RpcProviderBootstrap implements BeanPostProcessor {

    /**
     * bean初始化后执行，注册服务
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        DRpcService dRpcService = beanClass.getAnnotation(DRpcService.class);
        if (dRpcService != null) {
            System.out.println("需要注册服务...");
            Class<?> interfaceClass = dRpcService.interfaceClass();

            if (interfaceClass == void.class) {
                interfaceClass = beanClass.getInterfaces()[0];
            }
            String serviceName = interfaceClass.getName();
            String version = dRpcService.version();

            System.out.println("注册服务...");
            LocalRegistry.register(serviceName, beanClass);

            final RpcConfig rpcConfig = RpcApplication.getRpcConfig();

            System.out.println("正在注册服务到注册中心...");
            RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
            Registry registry = RegistryFactory.getRegistry(registryConfig.getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceHost(rpcConfig.getHost());
            serviceMetaInfo.setServicePort(rpcConfig.getPort());
            serviceMetaInfo.setServiceVersion(rpcConfig.getVersion());
            try {
                registry.register(serviceMetaInfo);
            } catch (Exception e) {
                throw new RuntimeException("服务注册失败" + e);
            }
        }
        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }
}

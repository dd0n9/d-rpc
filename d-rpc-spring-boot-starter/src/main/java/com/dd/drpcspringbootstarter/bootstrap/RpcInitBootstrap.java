package com.dd.drpcspringbootstarter.bootstrap;

import com.dd.drpc.RpcApplication;
import com.dd.drpc.config.RpcConfig;
import com.dd.drpc.server.HttpServer;
import com.dd.drpc.server.VertxHttpServer;
import com.dd.drpcspringbootstarter.annotation.EnableDRpc;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * Rpc框架启动
 */
public class RpcInitBootstrap implements ImportBeanDefinitionRegistrar {

    /**
     * Spring启动时执行，初始化Rpc框架
     * @param importingClassMetadata
     * @param registry
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        // 获取@EnableRpc注解属性
        System.out.println("获取@EnableRpc属性...");
        boolean needServer = (boolean) importingClassMetadata.getAnnotationAttributes(EnableDRpc.class.getName())
                .get("needServer");

        System.out.println("Rpc初始化...");
        RpcApplication.init();

        final RpcConfig rpcConfig = RpcApplication.getRpcConfig();

        if (needServer) {
            System.out.println("启动server...");
            HttpServer httpServer = new VertxHttpServer();
            httpServer.doStart(rpcConfig.getPort());
        }
        else {
            System.out.println("不启动server...");
        }
    }
}

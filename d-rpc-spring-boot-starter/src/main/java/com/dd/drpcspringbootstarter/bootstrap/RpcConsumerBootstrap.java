package com.dd.drpcspringbootstarter.bootstrap;

import com.dd.drpc.proxy.ProxyFactory;
import com.dd.drpcspringbootstarter.annotation.DRpcReference;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Field;

/**
 * 服务消费者启动类
 */
public class RpcConsumerBootstrap implements BeanPostProcessor {

    /**
     * bean生成后启动，注入服务
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        Field[] fields = beanClass.getDeclaredFields();
        for (Field field : fields) {
            DRpcReference dRpcReference = field.getAnnotation(DRpcReference.class);
            if (dRpcReference != null) {
                System.out.println("正在注入服务...");
                Class<?> interfaceClass = dRpcReference.interfaceClass();
                if (interfaceClass == void.class) {
                    interfaceClass = field.getType();
                }
                field.setAccessible(true);
                Object proxy = ProxyFactory.getProxy(interfaceClass);
                try {
                    field.set(bean, proxy);
                    field.setAccessible(false);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("注入服务失败", e);
                }
            }
        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}

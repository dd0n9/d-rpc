package com.dd.drpc.proxy;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 模拟调用代理
 */
@Slf4j
public class MockServiceProxy implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?> returnType = method.getReturnType();
        log.info("mock invoke {}", method.getName());
        return getDeafultObject(returnType);
    }

    /**
     * 生成默认返回对象
     * @param returnType
     * @return
     */
    private Object getDeafultObject(Class<?> returnType) {
        if (returnType.isPrimitive()) {
            if (returnType == boolean.class) {
                return false;
            }
            if (returnType == byte.class) {
                return 0;
            }
            if (returnType == char.class) {
                return 0;
            }
            if (returnType == short.class) {
                return 0;
            }
            if (returnType == int.class) {
                return 0;
            }
            if (returnType == long.class) {
                return 0L;
            }
            if (returnType == float.class) {
                return 0;
            }
            if (returnType == double.class) {
                return 0;
            }
            if (returnType == void.class) {
                return null;
            }
        }
        return null;
    }
}


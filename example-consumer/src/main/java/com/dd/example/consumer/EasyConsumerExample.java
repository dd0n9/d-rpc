package com.dd.example.consumer;

import com.dd.drpc.proxy.ProxyFactory;
import com.dd.example.common.model.User;
import com.dd.example.common.service.UserService;

/**
 * 简易服务消费者启动类
 */
public class EasyConsumerExample {
    public static void main(String[] args) {
        // 获取实例对象
        UserService userService = ProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("d0n9");
        User newUser = userService.getUser(user);
        if (newUser != null) {
            System.out.println("username:" + newUser.getName());
        } else {
            System.out.println("user not found");
        }
    }
}

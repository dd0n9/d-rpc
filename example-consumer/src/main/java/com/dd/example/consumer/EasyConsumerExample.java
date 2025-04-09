package com.dd.example.consumer;

import com.dd.example.common.model.User;
import com.dd.example.common.service.UserService;

/**
 * 简易服务消费者启动类
 */
public class EasyConsumerExample {
    public static void main(String[] args) {
        // TODO 获取UserService实例对象
        UserService userService = null;
        User user = new User();
        user.setName("d0n9");
        User newUser = userService.getUser(user);
        if (newUser != null) {
            System.out.println(newUser.getName());
        } else {
            System.out.println("user not found");
        }
    }
}

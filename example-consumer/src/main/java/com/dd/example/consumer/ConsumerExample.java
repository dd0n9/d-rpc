package com.dd.example.consumer;

import com.dd.drpc.config.RpcConfig;
import com.dd.drpc.proxy.ProxyFactory;
import com.dd.drpc.utils.ConfigUtils;
import com.dd.example.common.model.User;
import com.dd.example.common.service.UserService;

/**
 * 消费者启动类
 */
public class ConsumerExample {
    public static void main(String[] args) {
        RpcConfig rpc = ConfigUtils.loadConfig(RpcConfig.class, "rpc");
        System.out.println(rpc);

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

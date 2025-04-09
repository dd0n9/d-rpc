package com.dd.example.provider;

import com.dd.example.common.model.User;
import com.dd.example.common.service.UserService;

/**
 * 用户服务实现类
 */
public class UserServiceImpl implements UserService {

    @Override
    public User getUser(User user) {
        System.out.println("username:" + user.getName());
        return user;
    }
}

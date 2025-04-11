package com.dd.example.common.service;

import com.dd.example.common.model.User;

/**
 * 用户服务
 */
public interface UserService {

    /**
     * 获取用户
     *
     * @param user
     * @return
     */
    User getUser(User user);

    /**
     * 默认方法（测试Mock用的）
     * @return
     */
    default int getNumber(){
        return 1;
    }

}

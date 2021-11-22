package com.aike.service;

import com.aike.pojo.User;

/**
 * 定义中立的第三方接口
 */
public interface DubboUserService {

    void saveUser(User user);
}

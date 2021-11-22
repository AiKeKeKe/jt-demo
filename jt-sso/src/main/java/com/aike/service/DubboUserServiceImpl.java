package com.aike.service;

import com.aike.mapper.UserMapper;
import com.aike.pojo.User;
import com.aike.redis.BaseRedisCache;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 第三方接口的实现类
 */
@Service(timeout = 300)
public class DubboUserServiceImpl implements DubboUserService {
    @Resource
    private UserMapper userMapper;

    @Resource
    private BaseRedisCache baseRedisCache;

    @Override
    public void saveUser(User user) {
        // 密码加密
        String md5PassWord = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        // 封装数据
        user.setEmail(user.getPhone())
                .setPassword(md5PassWord)
                .setCreated(new Date())
                .setUpdated(user.getCreated());
        userMapper.insert(user);
    }
}

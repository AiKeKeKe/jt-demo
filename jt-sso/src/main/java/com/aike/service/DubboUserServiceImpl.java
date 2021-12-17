package com.aike.service;

import com.aike.mapper.UserMapper;
import com.aike.pojo.User;
import com.aike.redis.BaseRedisCache;
import com.aike.util.ObjectMapperUtil;
import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Objects;

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

    /**
     * 1.用户信息校验
     * 密码加密处理之后查询数据库
     * 2.校验用户数据.
     * 3.将数据保存到redis中.
     */
    @Override
    public String doLogin(User user) {
        String md5PassWord = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(md5PassWord);
        //将对象中不为null的属性当做where条件
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        User userDB = userMapper.selectOne(queryWrapper);
        String token = null;
        if (!Objects.isNull(userDB)) {
            // 将用户数据存redis
            String tokenTemp = "JT_TICKET_" + System.currentTimeMillis() + user.getUsername();
            tokenTemp = DigestUtils.md5DigestAsHex(tokenTemp.getBytes());
            userDB.setPassword("123456你猜对吗??");
            String userJSON = ObjectMapperUtil.toJSON(userDB);
            baseRedisCache.setKeyValueWithExpireTime(tokenTemp, userJSON, 7 * 24 * 3600);
            token = tokenTemp;
        }
        return token;
    }
}

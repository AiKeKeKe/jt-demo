package com.aike.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

public abstract class BaseRedisCache {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    public <T> T getValueByKey(String key) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        return (T) valueOperations.get(key);
    }

    public <T> void setKeyValue(String key, T value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public <T> void setKeyValueWithExpireTime(String key, T value, long time) {
        redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
    }

}

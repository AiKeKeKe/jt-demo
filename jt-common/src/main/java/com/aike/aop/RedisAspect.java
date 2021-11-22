package com.aike.aop;

import com.aike.anno.Cache_find;
import com.aike.enu.KEY_ENUM;
import com.aike.redis.BaseRedisCache;
import com.aike.util.ObjectMapperUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Aspect
@Component
@Slf4j
public class RedisAspect {

    @Autowired(required = false)
    private BaseRedisCache baseRedisCache;

    @Around(value = "@annotation(cache_find)")
    public Object around(ProceedingJoinPoint joinPoint, Cache_find cache_find) {
        //动态获取key
        String key = getKey(joinPoint, cache_find);
        String resultJSON = baseRedisCache.getValueByKey(key);
        Object resultData = null;
        try {
            if (StringUtils.isEmpty(resultJSON)) {
                resultData = joinPoint.proceed();
                String json = ObjectMapperUtil.toJSON(resultJSON);
                if (cache_find.second() == 0) {
                    baseRedisCache.setKeyValue(key, json);
                } else {
                    baseRedisCache.setKeyValueWithExpireTime(key, json, cache_find.second());
                }
            } else {
                Class resultType = getClass(joinPoint);
                resultData = ObjectMapperUtil.toObject(resultJSON, resultType);
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return resultData;
    }

    private Class getClass(ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        return methodSignature.getReturnType();
    }

    private String getKey(ProceedingJoinPoint joinPoint, Cache_find cache_find) {
        //判断key的类型
        if (KEY_ENUM.EMPTY.equals(cache_find.keyType())) {
            return cache_find.key();
        }
        String methodName = joinPoint.getSignature().getName();
        String arg0 = String.valueOf(joinPoint.getArgs()[0]);
        return methodName + "::" + arg0;
    }

}

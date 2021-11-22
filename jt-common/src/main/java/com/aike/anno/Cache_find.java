package com.aike.anno;

import com.aike.enu.KEY_ENUM;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Cache_find {
    String key() default "";//接收用户key值
    KEY_ENUM keyType() default KEY_ENUM.AUTO;//定义key类型
    int second() default 0;//永不失效

}

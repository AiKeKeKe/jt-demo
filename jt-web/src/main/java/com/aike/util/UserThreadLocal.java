package com.aike.util;

import com.aike.pojo.User;

public class UserThreadLocal {
    private static ThreadLocal<User> threadLocal = new ThreadLocal<>();
    public static User get(){
        return threadLocal.get();
    }
    public static void set(User user){
        threadLocal.set(user);
    }
    public static void remove(){
        threadLocal.remove();
    }

}

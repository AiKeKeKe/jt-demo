package com.aike.Interceptor;

import com.aike.pojo.User;
import com.aike.redis.BaseRedisCache;
import com.aike.util.ObjectMapperUtil;
import com.aike.util.UserThreadLocal;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * boolean:
 * 		true: 表示放行
 * 		false:表示拦截  一般配合重定向使用
 *
 * 业务实现步骤
 * 	1.获取用户Cookie中的token信息.
 * 	2.校验数据是否有效
 * 	3.校验redis中是否有数据
 * 	如果上述操作正确无误.返回true
 * 	否则return false 重定向到登录页面
 *
 */
@Component
public class UserInterceptor implements HandlerInterceptor {

    @Resource
    private BaseRedisCache baseRedisCache;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        String token = null;
        if (cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if ("JT_TICKET".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }
        if (StringUtils.isNotEmpty(token)) {
            String userJson = baseRedisCache.getValueByKey(token);
            if (StringUtils.isNotEmpty(userJson)) {
                User user = ObjectMapperUtil.toObject(userJson, User.class);
                //user信息共享方式：1.通过request对象封装，2.利用threadLocal实现数据共享，这里使用第二种
//                request.setAttribute("JT_USER", user);
                UserThreadLocal.set(user);
                return true;
            }
        }
        response.sendRedirect("/user/login.html");
        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserThreadLocal.remove();
    }
}

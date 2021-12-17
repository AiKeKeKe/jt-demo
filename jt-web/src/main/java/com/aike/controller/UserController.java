package com.aike.controller;

import com.aike.pojo.User;
import com.aike.redis.BaseRedisCache;
import com.aike.service.DubboUserService;
import com.aike.vo.SysResult;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/user")
public class UserController {

    @Reference(timeout = 3000, check = false)
    private DubboUserService dubboUserService;

    @Resource
    private BaseRedisCache baseRedisCache;

    //通用页面跳转
    @RequestMapping("/{moduleName}")
    public String moduleName(@PathVariable String moduleName) {
        return moduleName;
    }

    @RequestMapping("/doRegister")
    @ResponseBody
    public SysResult saveUser(User user) {
        //利用dubbo rpc协议完成远程过程调用
        dubboUserService.saveUser(user);
        return SysResult.success();
    }

    /**
     * 1.关于Cookie声明周期问题
     * cookie.setMaxAge(>0); cookie数据存活100秒
     * cookie.setMaxAge(0);	 表示删除cookie
     * cookie.setMaxAge(-1); 会话关闭cookie删除
     *
     * 2.表示Cookie使用的权限问题
     * www.baidu.com
     * cookie.setPath("/");
     * www.baidu.com/aa/1.html  可以访问
     * cookie.setpath("/bb")
     * www.baidu.com/aa/1.html	不可以访问
     *
     * 3.设定cookie共享
     * 	规定:每一个网址都有自己固定的Cookie信息.默认不能共享
     * 	需求:
     * 		www.jd.com	一级域名
     * 		item.jd.com	二级域名
     * 	要求在一级域名与二级域名实现cookie数据共享.
     *  实现步骤:
     *  	可以设定domain标签实现cookie共享.
     * @param user
     * @param response
     * @return
     */
    @RequestMapping("/doLogin")
    @ResponseBody
    public SysResult doLogin(User user, HttpServletResponse response) {
        String token = dubboUserService.doLogin(user);
        if (StringUtils.isEmpty(token)) {
            return SysResult.fail();
        }
        // 将token数据写入cookie
        Cookie cookie = new Cookie("JT_TICKET", token);
        cookie.setMaxAge(7 * 24 * 3600);
        cookie.setPath("/");
        cookie.setDomain("aike.com");
        response.addCookie(cookie);
        return SysResult.success();
    }

    @RequestMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        String token = null;
        if (cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if("JT_TICKET".equals(cookie.getName())) {
                    //获取指定数据的值
                    token = cookie.getValue();
                    break;
                }
            }
        }
        if (StringUtils.isNotEmpty(token)) {
            baseRedisCache.remove(token);
            Cookie cookie = new Cookie("JT_TICKET", "");
            cookie.setMaxAge(0);
            cookie.setPath("/");
            cookie.setDomain("aike.com");
            response.addCookie(cookie);
        }
        return "redirect:/";
    }

}

package com.aike.controller;

import com.aike.pojo.User;
import com.aike.service.DubboUserService;
import com.aike.vo.SysResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class UserController {


    private DubboUserService dubboUserService;

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

}

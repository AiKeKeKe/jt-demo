package com.aike.aop;

import com.aike.vo.SysResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class SysResultAspect {
    @ExceptionHandler({RuntimeException.class})
    public SysResult sysResultFail(Exception e){
        e.printStackTrace();
        log.error("服务器异常信息:" + e.getMessage());
        return SysResult.fail();
    }
}

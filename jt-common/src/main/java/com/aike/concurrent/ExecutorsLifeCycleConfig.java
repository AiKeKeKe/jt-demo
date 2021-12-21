package com.aike.concurrent;

import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

/**
 * 使用ConfigBean的方式管理MyExecutors生命周期，主要是清理逻辑
 */
@Component
public class ExecutorsLifeCycleConfig {

    @PreDestroy
    private void destroy() {
        MyExecutors.get().destroy();
    }
}

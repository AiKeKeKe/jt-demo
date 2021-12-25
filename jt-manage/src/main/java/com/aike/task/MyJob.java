package com.aike.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MyJob {
    @Scheduled(cron = "*/5 * * * * ?")
    public void baseHandle() {
        System.out.println("task begin");
    }
}

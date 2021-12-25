package com.aike;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@MapperScan("com.aike.mapper")
public class SpringBootRun {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootRun.class, args);
    }
}

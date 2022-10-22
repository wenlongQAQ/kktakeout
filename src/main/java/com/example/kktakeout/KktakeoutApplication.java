package com.example.kktakeout;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@SpringBootApplication
@ServletComponentScan //扫描过滤器
@EnableTransactionManagement
public class KktakeoutApplication {

    public static void main(String[] args) {
        SpringApplication.run(KktakeoutApplication.class, args);
        log.info("启动成功");
    }

}

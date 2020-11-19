package com.wru.onthi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.wru.onthi"})
public class OnthiOnlineApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnthiOnlineApplication.class, args);
    }

}

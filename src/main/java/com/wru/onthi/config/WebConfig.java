package com.wru.onthi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry)
    {
        registry.addViewController("/").setViewName("home");
        registry.addViewController("/login/login.html").setViewName("login/login");
        //registry.addViewController("/home").setViewName("userhome");
        registry.addViewController("/403/html").setViewName("403");
    }
}

package com.wru.onthi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
    @GetMapping(value = {"/","/home"})
    public String home(){
        return "home";
    }

    @GetMapping("/login")
    public String login(){
        return "login/login";
    }

}

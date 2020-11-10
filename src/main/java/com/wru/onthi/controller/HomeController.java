package com.wru.onthi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;


@Controller
public class HomeController {
    @GetMapping(value = {"/","/home"})
    public String index(){
        return "index";
    }
//    @GetMapping(value = {"/home"})
//    public String home(Principal principal, Model model) {
//        String name = principal.getName();
//        model.addAttribute("username", name);
//        return "home";
//    }




}

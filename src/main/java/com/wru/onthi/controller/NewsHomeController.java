package com.wru.onthi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NewsHomeController {

    @GetMapping("/tintuc")
    public String getNews(){
        return "news/content-news";
    }
}

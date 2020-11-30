package com.wru.onthi.controller;

import com.sun.org.apache.xpath.internal.operations.Neg;
import com.wru.onthi.entity.CategoryNews;
import com.wru.onthi.entity.Classroom;
import com.wru.onthi.entity.News;
import com.wru.onthi.services.ClassroomService;
import com.wru.onthi.services.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class NewsHomeController {

    @Autowired
    NewsService newsService;
    @Autowired
    ClassroomService classroomService;

    @GetMapping("/tintuc/list-news")
    public String getListNews(Model model, Principal principal, Pageable pageable){

        List<Classroom> listClass = classroomService.getAllClassroom();
        if(!listClass.isEmpty()){
            model.addAttribute("listClass",listClass);
        }

        int pageNumber = pageable.getPageNumber();
        int pageSize= 5;
        pageNumber = (pageNumber < 1 ? 1 : pageNumber) - 1;
        Pageable pageItem = PageRequest.of(pageNumber, pageSize);
        Page<News> pageNews= newsService.getAllNews(pageItem);
        model.addAttribute("pageInfo", pageNews);
        model.addAttribute("path","/tintuc/list-news");
        return "news/list-news";
    }

    @GetMapping("/tintuc")
    public String getInfoNews(@RequestParam("newsId") Integer newsId,Model model,Principal principal){
        Optional<News> optionalNews= newsService.findByNewsId(newsId);
        News news= optionalNews.get();
        model.addAttribute("news",news);

        return "news/content-news";
    }
}

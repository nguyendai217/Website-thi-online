package com.wru.onthi.controller;

import com.sun.org.apache.xpath.internal.operations.Neg;
import com.wru.onthi.entity.CategoryNews;
import com.wru.onthi.entity.Classroom;
import com.wru.onthi.entity.News;
import com.wru.onthi.services.CategoryNewsService;
import com.wru.onthi.services.ClassroomService;
import com.wru.onthi.services.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    @Autowired
    CategoryNewsService categoryNewsService;

    @GetMapping("/tintuc/list-news")
    public String getListNews(Model model, Principal principal, Pageable pageable) {

        List<News> getNewsOrderByViews = newsService.getListNewsOrderByViews().subList(0, 5);
        model.addAttribute("newshot", getNewsOrderByViews);

        List<CategoryNews> listCategory = categoryNewsService.getlistCategoryNews();
        model.addAttribute("category", listCategory);
        List<Classroom> listClass = classroomService.getAllClassroom();
        if (!listClass.isEmpty()) {
            model.addAttribute("listClass", listClass);
        }

        int pageNumber = pageable.getPageNumber();
        int pageSize = 5;
        pageNumber = (pageNumber < 1 ? 1 : pageNumber) - 1;
        Pageable pageItem = PageRequest.of(pageNumber, pageSize, Sort.by("id").descending());
        Page<News> pageNews = newsService.getAllNews(pageItem);
        model.addAttribute("pageInfo", pageNews);
        model.addAttribute("path", "/tintuc/list-news");
        return "news/list-news";
    }

    @GetMapping("/tintuc")
    public String getInfoNews(@RequestParam("newsId") Integer newsId, Model model, Principal principal) {
        Optional<News> optionalNews = newsService.findByNewsId(newsId);
        News news = optionalNews.get();
        Integer view = news.getViews();

        List<Classroom> listClass = classroomService.getAllClassroom();
        if (!listClass.isEmpty()) {
            model.addAttribute("listClass", listClass);
        }
        // update view
        news.setViews(view + 1);
        newsService.updateNews(news);

        model.addAttribute("news", news);
        return "news/content-news";
    }

    @GetMapping("/tintuc/list-news/{category}")
    public String getNewsByCcategory(@PathVariable("category") Integer category,
                                     Model model, Principal principal){

        return "";
    }
}

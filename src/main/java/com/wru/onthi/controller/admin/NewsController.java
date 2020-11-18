package com.wru.onthi.controller.admin;

import com.wru.onthi.entity.CategoryNews;
import com.wru.onthi.entity.News;
import com.wru.onthi.entity.User;
import com.wru.onthi.services.CategoryNewsService;
import com.wru.onthi.services.NewsService;
import com.wru.onthi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/news")
public class NewsController {

    @Autowired
    CategoryNewsService categoryNewsService;

    @Autowired
    UserService userService;

    @Autowired
    NewsService newsService;

    // list- category
    @GetMapping("/list-category")
    public String listCategoryNews(Model model, Principal principal, Pageable pageable){
        getInfoUser(model,principal);

        int pageNumber = pageable.getPageNumber();
        int pageSize= 5;
        pageNumber = (pageNumber < 1 ? 1 : pageNumber) - 1;
        Pageable pageItem = PageRequest.of(pageNumber, pageSize);
        Page<CategoryNews> categoryNews = categoryNewsService.getAllCategoryNews(pageItem);

        int totalItem = (int) categoryNews.getTotalElements();
        int itemPerPage= pageSize * (pageNumber+1);
        if(itemPerPage > totalItem){
            itemPerPage= totalItem;
        }

        Page<CategoryNews> pageCategoryNews = categoryNewsService.getAllCategoryNews(pageItem);
        if(pageCategoryNews == null){
            model.addAttribute("error","Danh sách thể loại trống.");
            return "admin/news/list-category";
        } else {
            model.addAttribute("pageInfo", pageCategoryNews);
            model.addAttribute("pageInfo", categoryNews);
            model.addAttribute("total",totalItem);
            model.addAttribute("itemPerPage",itemPerPage);
            model.addAttribute("path","/news/list-category");
            return "admin/news/list-category";
        }
    }

    @PostMapping("/add-category")
    public String addCategoryPost(RedirectAttributes redir,@RequestParam(value = "categoryname") String categoryName) {
        CategoryNews categoryNews = new CategoryNews();

        if(!categoryName.isEmpty()){
            categoryNews.setCategoryName(categoryName);
            categoryNews.setStatus(1);
            try {
                categoryNewsService.createCategory(categoryNews);
                redir.addFlashAttribute("success","Thêm thể loại tin tức thành công");
            }catch (Exception e){
                redir.addFlashAttribute("error","Thêm thể loại tin tức thất bại");
            }
        }else {
            redir.addFlashAttribute("error","Thêm thể loại tin tức thất bại");
        }

        return "redirect:/news/list-category";
    }

    // delete category
    @GetMapping("/delete-category/{id}")
    public String deleteCategory(RedirectAttributes redir,Model model,
                                 Principal principal,
                                 @PathVariable(value = "id") Integer id) {
        getInfoUser(model,principal);

        Optional<CategoryNews> optionalCategory= categoryNewsService.findByCategoryNewsId(id);
        CategoryNews categoryNews= optionalCategory.get();

        try {
            categoryNewsService.deleteCategory(categoryNews);
            redir.addFlashAttribute("success","Xoá thể loại thành công");
        }catch (Exception e){
            redir.addFlashAttribute("error","Xoá thể loại thất bại");
        }
        return "redirect:/news/list-category";
    }

    //update category
    @GetMapping("/update-category/{id}")
    public String updateCategoryGet(Model model,Principal principal,@PathVariable("id") Integer id){
        getInfoUser(model,principal);

        Optional<CategoryNews> optional= categoryNewsService.findByCategoryNewsId(id);
        CategoryNews categoryNews= optional.get();

        model.addAttribute("category",categoryNews);
        return "admin/news/update-category";
    }

    @PostMapping("/update-category")
    public String updateCategoryPost(Model model,Principal principal,RedirectAttributes redir,
                                     @RequestParam("categoryId") Integer id,
                                     @RequestParam("categoryName") String name,
                                     @RequestParam("status") Integer status){
        getInfoUser(model,principal);

        Optional<CategoryNews> optionalCategoryNews= categoryNewsService.findByCategoryNewsId(id);
        CategoryNews categoryNews = optionalCategoryNews.get();

        try {
            categoryNews.setStatus(status);
            categoryNews.setCategoryName(name);
            categoryNewsService.updateCategory(categoryNews);
            redir.addFlashAttribute("success","Cập nhật thể loại thành công");
        }catch (Exception e){
            redir.addFlashAttribute("error","Cập nhật thể loại thất bại");
        }
        return "redirect:/news/list-category";

    }
    //list news
    @GetMapping("/list-news")
    public String listNews(Model model,Principal principal,Pageable pageable){
        getInfoUser(model,principal);

        //get list category
        List<CategoryNews> listCategory= categoryNewsService.getlistCategoryNews();
        model.addAttribute("listCategory",listCategory);

        // pageable list news
        int pageNumber = pageable.getPageNumber();
        int pageSize=5;
        pageNumber = (pageNumber < 1 ? 1 : pageNumber) - 1;
        Pageable pageItem = PageRequest.of(pageNumber, pageSize);

        Page<News> pageNews = newsService.getAllNews(pageItem);
        int totalItem = (int) pageNews.getTotalElements();
        int itemPerPage= pageSize * (pageNumber +1);
        if(itemPerPage > totalItem){
            itemPerPage= totalItem;
        }
        if(pageNews == null){
            model.addAttribute("error","Danh sách tin tức trống.");
            return "admin/news/list-news";
        } else {
            model.addAttribute("pageInfo", pageNews);
            model.addAttribute("total",totalItem);
            model.addAttribute("itemPerPage",itemPerPage);
            model.addAttribute("path","/news/list-news");
            return "admin/news/list-news";
        }
    }

    // add new
    @GetMapping("/add-news")
    public String addNewsGet(Model model, Principal principal){
        getInfoUser(model,principal);
        List<CategoryNews> listCategory= categoryNewsService.getlistCategoryNews();
        model.addAttribute("listCategory",listCategory);
        return "admin/news/add-news";
    }

    // add new
    @GetMapping("/update-news/{newsId}")
    public String updateNewsGet(Model model, Principal principal,
                                @PathVariable("newsId") Integer newsID){
        getInfoUser(model,principal);
        List<CategoryNews> listCategory= categoryNewsService.getlistCategoryNews();
        model.addAttribute("listCategory",listCategory);

        Optional<News> optionalNews= newsService.findByNewsId(newsID);
        News news= optionalNews.get();
        model.addAttribute("news",news);
        return "admin/news/update-news";
    }

    @PostMapping("/add-news")
    public String addNewsPost(Model model,Principal principal){
        getInfoUser(model,principal);
        return "";
    }

    // detail news
    @GetMapping("/detail-news/{id}")
    public String detailNews(Model model, Principal principal, @PathVariable(value = "id") Integer id){
        getInfoUser(model,principal);
        Optional<News> optional = newsService.findByNewsId(id);
        News news= optional.get();
        model.addAttribute("news",news);
        return "admin/news/detail-news";
    }

    // delete news
    @GetMapping("/delete-news/{id}")
    public String deleteNews(Model model, RedirectAttributes redir,
                             Principal principal,
                             @PathVariable(value = "id") Integer id){
        getInfoUser(model,principal);

        Optional<News> optional = newsService.findByNewsId(id);
        News news= optional.get();
        try {
            newsService.deleteNews(news);
            redir.addFlashAttribute("success","Xóa tin tức thành công");
            return "redirect:/news/list-news";
        }catch (Exception e){
            redir.addFlashAttribute("error","Xóa tin tức thất bại");
            return "redirect:/news/list-news";
        }
    }

    // get info user login
    private void getInfoUser(Model model,Principal principal){
        String username= principal.getName();

        if(username != null){
            User user= userService.findUserByName(username);
            String email= user.getEmail();
            String image= user.getImage();
            model.addAttribute("image",image);
            model.addAttribute("username", username);
            model.addAttribute("email", email);
        }
    }
}

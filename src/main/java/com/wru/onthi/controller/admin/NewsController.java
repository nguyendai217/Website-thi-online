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

        // pageable list user
        int pageNumber = pageable.getPageNumber();
        pageNumber = (pageNumber < 1 ? 1 : pageNumber) - 1;
        Pageable pageItem = PageRequest.of(pageNumber, 5);

        Page<CategoryNews> pageCategoryNews = categoryNewsService.getAllCategoryNews(pageItem);
        if(pageCategoryNews == null){
            model.addAttribute("error","Danh sách thể loại trống.");
            return "admin/news/list-category-news";
        } else {
            model.addAttribute("pageInfo", pageCategoryNews);
            return "admin/news/list-category-news";
        }
    }

    // add category
    @GetMapping("/add-category-news")
    public String addCategoryGet(Model model, Principal principal){
        getInfoUser(model,principal);
        return "admin/news/add-category-news";
    }

    @PostMapping("/add-category-news")
    public String addCategoryPost(Model model, Principal principal, RedirectAttributes redir,
                                  @RequestParam(value = "categoryName",defaultValue = "") String categoryName){
        getInfoUser(model,principal);
        CategoryNews categoryNews= new CategoryNews();
        categoryNews.setCategoryName(categoryName);
        categoryNews.setStatus(1);
        try {
            categoryNewsService.createCategory(categoryNews);
            redir.addFlashAttribute("success","Thêm thể loại thành công");
            return "redirect:/news/list-category";
        }catch (Exception e){
            model.addAttribute("error","Thêm thể loại thất bại");
            return "admin/news/add-category-news";
        }
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
                                     @RequestParam("categoryName") String name){
        getInfoUser(model,principal);

        Optional<CategoryNews> optionalCategoryNews= categoryNewsService.findByCategoryNewsId(id);
        CategoryNews categoryNews = optionalCategoryNews.get();

        try {
            categoryNews.setCategoryName(name);
            categoryNewsService.updateCategory(categoryNews);
            redir.addFlashAttribute("success","Cập nhật thể loại thành công");
        }catch (Exception e){
            redir.addFlashAttribute("error","Cập nhật thể loại thất bại");
        }

        return "redirect:/news/list-category";
    }
    //news
    @GetMapping("/list-news")
    public String listNews(Model model,Principal principal,Pageable pageable){
        getInfoUser(model,principal);

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
            return "admin/news/list-news";
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

package com.wru.onthi.controller.admin;

import com.wru.onthi.entity.CategoryNews;
import com.wru.onthi.entity.News;
import com.wru.onthi.entity.User;
import com.wru.onthi.services.CategoryNewsService;
import com.wru.onthi.services.NewsService;
import com.wru.onthi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Date;
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

    @Value("${folder.upload}")
    String foldeUpload;

    // list- category
    @GetMapping("/list-category")
    public String listCategoryNews(Model model, Principal principal, Pageable pageable,String category){
        getInfoUser(model,principal);

        int pageNumber = pageable.getPageNumber();
        int pageSize= 5;
        pageNumber = (pageNumber < 1 ? 1 : pageNumber) - 1;
        Pageable pageItem = PageRequest.of(pageNumber, pageSize);
        Page<CategoryNews> categoryNews= null;
        if(category == null){
            categoryNews = categoryNewsService.getAllCategoryNews(pageItem);
            model.addAttribute("path","/news/list-category");
        }else {
            categoryNews = categoryNewsService.searchCategory(category,pageItem);
        }

        int totalItem = (int) categoryNews.getTotalElements();
        int itemPerPage= pageSize * (pageNumber+1);
        if(itemPerPage > totalItem){
            itemPerPage= totalItem;
        }

        if(categoryNews == null){
            model.addAttribute("error","Danh sách thể loại trống.");
            return "admin/news/list-category";
        } else {
            model.addAttribute("pageInfo", categoryNews);
            model.addAttribute("total",totalItem);
            model.addAttribute("itemPerPage",itemPerPage);

            return "admin/news/list-category";
        }
    }

    @PostMapping("/add-category")
    public String addCategoryPost(RedirectAttributes redir,
                                  @RequestParam(value = "categoryname") String categoryName) {
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
        try {
            categoryNewsService.deleteCategory(id);
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
    @GetMapping("/update-category-status")
    public String updateStatus(Model model,Principal principal,
                               @RequestParam("id") Integer id,
                               @RequestParam("status") Integer status,
                               RedirectAttributes redir){
        getInfoUser(model,principal);
        try {
            if(status==1){
                categoryNewsService.updateCategoryStatus(id,0);
            }else if(status==0){
                categoryNewsService.updateCategoryStatus(id,1);
            }
            redir.addFlashAttribute("success","Update trạng thái thành công.");
        }catch (Exception e){
            redir.addFlashAttribute("error","Update trạng thái thất bại");
        }
        return "redirect:/news/list-category";
    }

    //list news
    @GetMapping("/list-news")
    public String listNews(Model model,Principal principal,Pageable pageable,String categoryId, String title, HttpServletRequest request){
        getInfoUser(model,principal);

        //get list category
        List<CategoryNews> listCategory= categoryNewsService.getlistCategoryNews();
        model.addAttribute("listCategory",listCategory);

        // pageable list news
        int pageNumber = pageable.getPageNumber();
        int pageSize=5;
        pageNumber = (pageNumber < 1 ? 1 : pageNumber) - 1;
        Pageable pageItem = PageRequest.of(pageNumber, pageSize);
        Page<News> pageNews= null;
        if((categoryId== null || categoryId=="") && (title== null || title=="")){
            pageNews = newsService.getAllNews(pageItem);
            String path= request.getRequestURI();
            model.addAttribute("path",path);
        }else {
            pageNews = newsService.searchNews(title,categoryId,pageItem);
            model.addAttribute("title",title);
            if(categoryId !=""){
                model.addAttribute("categorySelected",Integer.valueOf(categoryId));

            }else {
                model.addAttribute("categorySelected",categoryId);
            }
            String path= request.getRequestURI();
            model.addAttribute("path",path);
        }

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

    // add new
    @GetMapping("/add-news")
    public String addNewsGet(Model model, Principal principal){
        getInfoUser(model,principal);
        List<CategoryNews> listCategory= categoryNewsService.getlistCategoryNews();
        model.addAttribute("listCategory",listCategory);
        return "admin/news/add-news";
    }

    @PostMapping("/add-news")
    public String addNewsPost(Model model, Principal principal, HttpServletRequest request,
                              RedirectAttributes redr,
                              @RequestParam("image") MultipartFile multipartFile){
        getInfoUser(model,principal);
        String username= principal.getName();
        User user= userService.findUserByName(username);
        String title= request.getParameter("title");
        String description= request.getParameter("description");
        String content= request.getParameter("content");
        Integer categoryId= Integer.valueOf(request.getParameter("categoryId"));
        String tag= request.getParameter("tags");
        Optional<CategoryNews> optional= categoryNewsService.findByCategoryNewsId(categoryId);

        UploadImageController uploadImageController= new UploadImageController();
        String imgname= uploadImageController.getImageName(multipartFile);

        try {
            News news= new News();
            news.setTitle(title);
            news.setContent(content);
            news.setStatus(1);
            news.setTag(tag);
            news.setViews(0);
            news.setImage(imgname);
            news.setUserNews(user);
            news.setInsertBy(username);
            news.setInsertDate(new Date());
            news.setDescription(description);
            news.setCategoryNews(optional.get());
            newsService.createNews(news);

            uploadImageController.uploadImage(multipartFile,imgname,foldeUpload,"news");
            redr.addFlashAttribute("success","Thêm mới tin tức thành công");
            return "redirect:/news/list-news";
        } catch (IOException e){
            e.printStackTrace();

            redr.addFlashAttribute("error","Thêm mới tin tức thất bại");
            return "admin/news/add-news";
        }
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

    @PostMapping("/update-news")
    public String updateNewsPost(Model model, Principal principal,HttpServletRequest request,
                                 RedirectAttributes redr){
        getInfoUser(model,principal);
        String username= principal.getName();
        Integer newsId= Integer.valueOf(request.getParameter("newsId"));
        String title= request.getParameter("title");
        String description= request.getParameter("description");
        String content= request.getParameter("content");
        Integer categoryId= Integer.valueOf(request.getParameter("categoryId"));
        String tags= request.getParameter("tags");
        Optional<CategoryNews> optional= categoryNewsService.findByCategoryNewsId(categoryId);
        CategoryNews categoryNews= optional.get();

        Optional<News> optionalNews= newsService.findByNewsId(newsId);
        News news= optionalNews.get();

        try {
            news.setTitle(title);
            news.setContent(content);
            news.setCategoryNews(categoryNews);
            news.setDescription(description);
            news.setTag(tags);
            news.setUpdateDate(new Date());
            news.setUpdateBy(username);
            newsService.updateNews(news);
            redr.addFlashAttribute("success","Update tin tức thành công");
            return "redirect:/news/list-news";
        }catch (Exception e){
            e.printStackTrace();
            redr.addFlashAttribute("error","Update tin tưc thất bại");
            String path="/news/add-news/"+newsId;
            return path;
        }
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
        try {
            newsService.deleteNews(id);
            redir.addFlashAttribute("success","Xóa tin tức thành công");
            return "redirect:/news/list-news";
        }catch (Exception e){
            redir.addFlashAttribute("error","Xóa tin tức thất bại");
            return "redirect:/news/list-news";
        }
    }

    @GetMapping("/update-new-status")
    public String updateNewsStatus(Model model,Principal principal,
                               @RequestParam("id") Integer id,
                               @RequestParam("status") Integer status,
                               RedirectAttributes redir){
        getInfoUser(model,principal);
        try {
            if(status==1){
                newsService.updateStatus(id,0);
                Optional<News> optionalNews= newsService.findByNewsId(id);
                News news= optionalNews.get();
                news.setUpdateBy(principal.getName());
                news.setUpdateDate(new Date());
                newsService.updateNews(news);

            }else if(status==0){
                newsService.updateStatus(id,1);
            }
            redir.addFlashAttribute("success","Update trạng thái thành công.");
        }catch (Exception e){
            redir.addFlashAttribute("error","Update trạng thái thất bại");
        }
        return "redirect:/news/list-news";
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

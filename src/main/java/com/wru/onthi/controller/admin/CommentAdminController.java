package com.wru.onthi.controller.admin;

import com.wru.onthi.entity.Comment;
import com.wru.onthi.entity.Lesson;
import com.wru.onthi.entity.User;
import com.wru.onthi.services.CommentService;
import com.wru.onthi.services.LessonService;
import com.wru.onthi.services.NewsService;
import com.wru.onthi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/comment-manager")
public class CommentAdminController {
    @Autowired
    UserService userService;

    @Autowired
    CommentService commentService;

    @Autowired
    LessonService lessonService;

    @Autowired
    NewsService newsService;


    @GetMapping("/list-comment")
    public String getListAllComment(Model model, Principal principal,
                                    String lessonId, String newsId, Pageable pageable){
        getInfoUser(model,principal);

        int pageNumber = pageable.getPageNumber();
        int pageSize= 5;
        pageNumber = (pageNumber < 1 ? 1 : pageNumber) - 1;
        Pageable pageItem = PageRequest.of(pageNumber, pageSize, Sort.by("id").descending());
        Page<Comment> pageComment= null;
        if((lessonId == "" || lessonId ==null) &&
                (newsId == "" || newsId == null)){
            pageComment = commentService.pageComment(pageItem);
        }else {
            pageComment = commentService.searchComment(lessonId,newsId,pageItem);
        }

        int totalItem = (int) pageComment.getTotalElements();
        int itemPerPage= pageSize * (pageNumber+1);
        if(itemPerPage > totalItem){
            itemPerPage= totalItem;
        }

        model.addAttribute("pageInfo", pageComment);
        model.addAttribute("total",totalItem);
        model.addAttribute("itemPerPage",itemPerPage);
        model.addAttribute("path","/comment-manager/list-comment");
        model.addAttribute("condition", ("lessonId="+lessonId+"&newsId="+newsId));
        return "admin/comment/list-comment";
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

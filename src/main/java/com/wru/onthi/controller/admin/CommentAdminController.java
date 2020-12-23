package com.wru.onthi.controller.admin;

import com.wru.onthi.entity.Comment;
import com.wru.onthi.entity.Lesson;
import com.wru.onthi.entity.News;
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
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

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

    @GetMapping("/list-comment-lesson")
    public String getListCommentLesson(Model model, Principal principal,
                                    String lessonId, Pageable pageable){
        getInfoUser(model,principal);

        //get lesson distinct
        List<Lesson> getListLessonDistinct= lessonService.getListLessonDistinct();
        model.addAttribute("lesson",getListLessonDistinct);

        int pageNumber = pageable.getPageNumber();
        int pageSize= 5;
        pageNumber = (pageNumber < 1 ? 1 : pageNumber) - 1;
        Pageable pageItem = PageRequest.of(pageNumber, pageSize, Sort.by("id").descending());
        Page<Comment> pageComment= null;
        if(lessonId == "" || lessonId ==null){
            pageComment = commentService.pageCommentByLesson(pageItem);
        }else {
            pageComment = commentService.searchCommentLesson(Integer.valueOf(lessonId),pageItem);
        }

        int totalItem = (int) pageComment.getTotalElements();
        int itemPerPage= pageSize * (pageNumber+1);
        if(itemPerPage > totalItem){
            itemPerPage = totalItem;
        }

        model.addAttribute("pageInfo", pageComment);
        model.addAttribute("total",totalItem);
        model.addAttribute("itemPerPage",itemPerPage);
        model.addAttribute("path","/comment-manager/list-comment-lesson");
        if(lessonId== null ){
            lessonId= "";
        }
        model.addAttribute("condition", "lessonId="+lessonId);
        return "admin/comment/list-comment_lesson";
    }

    @GetMapping("/list-comment-news")
    public String getListCommentNews(Model model, Principal principal,
                                     String newsId, Pageable pageable){
        getInfoUser(model,principal);

        //get news distinct
        List<News> getListNewsDistinct= newsService.getListNewsDistinct();
        model.addAttribute("news",getListNewsDistinct);

        int pageNumber = pageable.getPageNumber();
        int pageSize= 5;
        pageNumber = (pageNumber < 1 ? 1 : pageNumber) - 1;
        Pageable pageItem = PageRequest.of(pageNumber, pageSize, Sort.by("id").descending());
        Page<Comment> pageComment= null;
        if(newsId == "" || newsId ==null){
            pageComment = commentService.pageCommentByNews(pageItem);
        }else {
            pageComment = commentService.searchCommentNews(Integer.valueOf(newsId),pageItem);
        }

        int totalItem = (int) pageComment.getTotalElements();
        int itemPerPage= pageSize * (pageNumber+1);
        if(itemPerPage > totalItem){
            itemPerPage= totalItem;
        }
        model.addAttribute("pageInfo", pageComment);
        model.addAttribute("total",totalItem);
        model.addAttribute("itemPerPage",itemPerPage);
        model.addAttribute("path","/comment-manager/list-comment-news");
        if(newsId== null){
            newsId= "";
        }
        model.addAttribute("condition", ("newsId="+newsId));
        return "admin/comment/list-comment_news";
    }

    @GetMapping("/remove-comment/{commentId}/{action}")
    public String deleteComment(@PathVariable("commentId") Integer commentId,
                                @PathVariable("action") Integer action,
                                Model model, Principal principal, RedirectAttributes redir){
        getInfoUser(model,principal);
        Comment comment= commentService.findByCommentId(commentId);
        try {
            commentService.deleteComment(comment);
            redir.addFlashAttribute("success","Xóa bình luận thành công");
        } catch (Exception e){
            redir.addFlashAttribute("error","Xóa bình luận thất bại");
        }
        if(action==1){
            return "redirect:/comment-manager/list-comment-lesson";
        }
        else{
            return "redirect:/comment-manager/list-comment-news";
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

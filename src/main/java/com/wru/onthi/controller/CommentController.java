package com.wru.onthi.controller;

import com.wru.onthi.entity.Comment;
import com.wru.onthi.entity.User;
import com.wru.onthi.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    CommentService commentService;

    @GetMapping("/get-comment")
    public String getComment(@RequestParam(value = "lessonId",defaultValue = "") Integer lessonId,
                             @RequestParam(value = "newsId",defaultValue = "") Integer newsId){

            List<Comment> listComment= commentService.getListComment(1,null);
            Comment comment= listComment.get(1);
            String username= comment.getUserComment().getUsername();

            Integer x= 1;
            return "";
    }


}

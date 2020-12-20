package com.wru.onthi.controller;

import com.wru.onthi.entity.Comment;
import com.wru.onthi.entity.Lesson;
import com.wru.onthi.entity.News;
import com.wru.onthi.entity.User;
import com.wru.onthi.model.CommentInfo;
import com.wru.onthi.model.CommentVO;
import com.wru.onthi.response.CommentResponse;
import com.wru.onthi.services.CommentService;
import com.wru.onthi.services.LessonService;
import com.wru.onthi.services.NewsService;
import com.wru.onthi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    LessonService lessonService;

    @Autowired
    NewsService newsService;

    @Autowired
    CommentService commentService;

    @Autowired
    UserService userService;

    @GetMapping("/get-comment")
    @ResponseBody
    public CommentVO getComment(
            HttpServletResponse httpResponse,
            @RequestParam(value = "lessonId", defaultValue = "") Integer lessonId,
            @RequestParam(value = "newsId", defaultValue = "") Integer newsId,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "perPage", defaultValue = "5") Integer perPage
    ){
        httpResponse.setStatus(HttpServletResponse.SC_OK);
        CommentVO result = new CommentVO();
        List<CommentInfo> listCommentInfo = new ArrayList<>();

        List<Comment> listCommentObject = commentService.getListComment(lessonId,newsId, perPage, (page - 1) * perPage);
        Long totalComment= commentService.getTotalComment(lessonId,newsId);

        listCommentObject.forEach(object -> {
            CommentInfo objectCMT = new CommentInfo();
            objectCMT.setId(object.getId());
            objectCMT.setContentComment(object.getContentComment());
            objectCMT.setTimeComment(object.getTimeComment());
            objectCMT.setUserName(object.getUserComment().getFullname());
            objectCMT.setUserImage(object.getUserComment().getImage());

            listCommentInfo.add(objectCMT);
        });


        result.setPage(page);
        result.setPerPage(perPage);
        result.setTotalComent(Integer.valueOf(totalComment.toString()));
        result.setListComment(listCommentInfo);

        return result;
    }

    @PostMapping("/add-comment")
    @ResponseBody
    public CommentResponse addComment(
            Principal principal,
            HttpServletResponse httpResponse,
            @RequestParam(value = "lessonId", defaultValue = "") Integer lessonId,
            @RequestParam(value = "newsId", defaultValue = "") Integer newsId,
            @RequestParam(value = "content", defaultValue = "") String content
    ){
        Comment comment = new Comment();
        DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        User user = userService.findUserByName(principal.getName());
        comment.setContentComment(content);
        comment.setTimeComment(dateTimeFormat.format(now));
        comment.setUserComment(user);
        if (lessonId != null) {
            Lesson lesson= lessonService.getContentLesson(lessonId);
            comment.setLessonComment(lesson);
        } else {
            Optional<News> optionalNews= newsService.findByNewsId(newsId);
            comment.setNewsComment(optionalNews.get());
        }

        commentService.save(comment);
        CommentInfo commentInfo = new CommentInfo(comment);
        CommentResponse result = new CommentResponse(commentInfo);

        return result;
    }


}

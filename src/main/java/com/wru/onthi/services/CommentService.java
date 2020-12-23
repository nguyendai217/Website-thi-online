package com.wru.onthi.services;

import com.wru.onthi.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentService {
    List<Comment> getListComment(Integer lessonId,Integer newsId, Integer limit, Integer ofset);
    Long getTotalComment(Integer lessonId,Integer newsId);
    Comment findByCommentId(Integer commentId);
    void save(Comment comment);
    void deleteComment(Comment comment);
    Page<Comment> pageCommentByLesson(Pageable pageable);
    Page<Comment> searchCommentLesson(Integer lessonId, Pageable pageable);
    Page<Comment> pageCommentByNews(Pageable pageable);
    Page<Comment> searchCommentNews(Integer newsId, Pageable pageable);
}

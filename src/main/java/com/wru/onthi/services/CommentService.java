package com.wru.onthi.services;

import com.wru.onthi.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentService {
    List<Comment> getListComment(Integer lessonId,Integer newsId, Integer limit, Integer ofset);
    Long getTotalComment(Integer lessonId,Integer newsId);

    void save(Comment comment);

    Page<Comment> pageComment(Pageable pageable);
    Page<Comment> searchComment(String lessonId, String newsId, Pageable pageable);
}

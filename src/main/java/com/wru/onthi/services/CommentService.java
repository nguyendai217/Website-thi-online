package com.wru.onthi.services;

import com.wru.onthi.entity.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> getListComment(Integer lessonId,Integer newsId);
}

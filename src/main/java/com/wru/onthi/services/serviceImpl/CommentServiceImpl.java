package com.wru.onthi.services.serviceImpl;

import com.wru.onthi.entity.Comment;
import com.wru.onthi.repository.CommentRepo;
import com.wru.onthi.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    CommentRepo commentRepo;

    @Override
    public List<Comment> getListComment(Integer lessonId, Integer newsId, Integer limit, Integer ofset) {
        return commentRepo.getListComment(lessonId, newsId, limit, ofset);
    }

    @Override
    public Long getTotalComment(Integer lessonId, Integer newsId) {
        return commentRepo.TotalComment(lessonId, newsId);
    }

    @Override
    public void save(Comment comment) {
        commentRepo.save(comment);
    }


}

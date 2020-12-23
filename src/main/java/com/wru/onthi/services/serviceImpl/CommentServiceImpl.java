package com.wru.onthi.services.serviceImpl;

import com.wru.onthi.entity.Comment;
import com.wru.onthi.repository.CommentRepo;
import com.wru.onthi.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
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
    public Comment findByCommentId(Integer commentId) {
        return commentRepo.findById(commentId).get();
    }

    @Override
    public void save(Comment comment) {
        commentRepo.save(comment);
    }

    @Override
    public void deleteComment(Comment comment) {
        commentRepo.delete(comment);
    }

    @Override
    public Page<Comment> pageCommentByLesson(Pageable pageable) {
        return commentRepo.pageCommentByLesson(pageable);
    }

    @Override
    public Page<Comment> searchCommentLesson(Integer lessonId, Pageable pageable) {
        return commentRepo.searchCommentLesson(lessonId,pageable);
    }

    @Override
    public Page<Comment> pageCommentByNews(Pageable pageable) {
        return commentRepo.pageCommentByNews(pageable);
    }

    @Override
    public Page<Comment> searchCommentNews(Integer newsId, Pageable pageable) {
        return commentRepo.searchCommentNews(newsId,pageable);
    }

}

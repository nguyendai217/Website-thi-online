package com.wru.onthi.repository;

import com.wru.onthi.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepo extends JpaRepository<Comment, Integer> {
    @Query("select e from Comment e " +
            "where (:lessonId is null or e.lessonComment.id = :lessonId) " +
            " or ( :newsId is null or e.newsComment.id = :newsId)")
    List<Comment> getListComment(Integer lessonId, Integer newsId);
}

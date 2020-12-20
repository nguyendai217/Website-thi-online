package com.wru.onthi.repository;

import com.wru.onthi.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepo extends JpaRepository<Comment, Integer> {
    @Query(value = "select e.* from comment e " +
            "where (e.lesson_id = :lessonId) " +
            "or (e.news_id = :newsId) " +
            "ORDER BY e.time_comment DESC " +
            "LIMIT :limit OFFSET :ofset ", nativeQuery = true)
    List<Comment> getListComment(Integer lessonId, Integer newsId, Integer limit, Integer ofset);

    @Query(value = "select count(*) from comment e " +
            "where (e.lesson_id = :lessonId) " +
            " or (e.news_id = :newsId)", nativeQuery = true)
    Long TotalComment(Integer lessonId, Integer newsId);
}

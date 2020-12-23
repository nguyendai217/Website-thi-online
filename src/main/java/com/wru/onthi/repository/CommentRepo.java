package com.wru.onthi.repository;

import com.wru.onthi.entity.Comment;
import com.wru.onthi.entity.Result;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("select c from Comment c where c.lessonComment.id =: lessonId or c.newsComment.id =: newsId")
    Page<Comment> pageComment(@Param("lessonId") String lessonId, @Param("newsId") String newsId, Pageable pageable);

    @Query(value = "select u from Comment u, News n where u.newsComment.id = n.id ")
    Page<Comment> pageCommentByNews(Pageable pageable);

    @Query(value = "select * from comment where news_id =?1 ",nativeQuery = true)
    Page<Comment> searchCommentNews(@Param("newsId") Integer newsId, Pageable pageable);

    @Query(value = "select u from Comment u, Lesson ls where u.lessonComment.id = ls.id")
    Page<Comment> pageCommentByLesson(Pageable pageable);

    @Query(value = "select * from comment where lesson_id =?1 ",nativeQuery = true)
    Page<Comment> searchCommentLesson(@Param("lessonId") Integer lessonId, Pageable pageable);

}

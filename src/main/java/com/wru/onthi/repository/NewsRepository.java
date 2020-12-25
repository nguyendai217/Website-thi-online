package com.wru.onthi.repository;

import com.wru.onthi.entity.Comment;
import com.wru.onthi.entity.Lesson;
import com.wru.onthi.entity.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News,Integer> {

    @Query("select e from News e where e.status=1 order by e.insertDate desc ")
    List<News> getListNewsOrderByTime();

    @Query(value = "select e from News e where e.categoryNews.id =4 and e.status=1 order by e.id desc ",nativeQuery = false)
    List<News> getNewsStudy();

    @Query(value = "select  * from news where title like %:title% " +
            "or category_id like %:category%",nativeQuery = true)
    Page<News> searchNews(String title, String category, Pageable pageable);

    @Modifying
    @Transactional
    @Query("update News n set n.status=0 where n.id =:id")
    void disableNews(@Param("id") Integer id);

    @Modifying
    @Transactional
    @Query("update News n set n.status=:status where n.id=:id")
    void updateStatus(@Param("id") Integer id, @Param("status") Integer status);

    @Query("select e from News e where e.status=1 order by e.views desc ")
    List<News> getListNewsOrderByViews();
    @Query("select e from News e where e.status=1 order by e.id desc ")
    List<News> getListNewsOrderById();

    @Query(value = "select distinct n from Comment cm, News n where cm.newsComment.id= n.id")
    List<News> getListNewsDistinct();
}

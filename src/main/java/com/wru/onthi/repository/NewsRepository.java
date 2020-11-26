package com.wru.onthi.repository;

import com.wru.onthi.entity.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News,Integer> {

    @Query("select e from News e order by e.insertDate desc ")
    List<News> getListNewsOrderByTime();

    @Query(value = "select e from News e where e.categoryNews.id =4 ",nativeQuery = false)
    List<News> getNewsBanner();

    @Query(value = "select  * from news where title like %:title% " +
            "or category_id like %:category%",nativeQuery = true)
    Page<News> searchNews(String title, String category, Pageable pageable);
}

package com.wru.onthi.repository;

import com.wru.onthi.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News,Integer> {

    @Query("select e from News e order by e.insertDate desc ")
    List<News> getListNewsOrderByTime();
}

package com.wru.onthi.services;

import com.wru.onthi.entity.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface NewsService {
    void createNews(News news);
    News updateNews(News news);
    void disableNews(Integer id);
    void deleteNews(News news);
    void updateStatus(Integer id, Integer status);
    Optional<News> findByNewsId(Integer id);
    List<News> getlistNews();
    Page<News> getAllNews(Pageable pageable);
    List<News> getListNewsOrderByTime();
    List<News> getNewsStudy();
    Page<News> searchNews(String title,String category, Pageable pageable);
    long countNews();
    List<News> getListNewsOrderByViews();
    List<News> getListNewsOrderById();
    List<News> getListNewsByCategory();
    List<News> getListNewsDistinct();
}
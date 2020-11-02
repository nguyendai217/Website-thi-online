package com.wru.onthi.services;

import com.wru.onthi.entity.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface NewsService {
    void createNews(News news);
    News updateNews(News news);
    void deleteNews(News news);
    Optional<News> findByNewsId(Integer id);
    List<News> getlistNews();
    Page<News> getAllNews(Pageable pageable);
}
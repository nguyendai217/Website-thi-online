package com.wru.onthi.services.serviceImpl;

import com.wru.onthi.entity.News;
import com.wru.onthi.repository.NewsRepository;
import com.wru.onthi.services.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NewsServiceImpl implements NewsService {
    @Autowired
    NewsRepository newsRepository;

    @Override
    public void createNews(News news) {
        newsRepository.save(news);
    }

    @Override
    public News updateNews(News news) {
        return newsRepository.save(news);
    }

    @Override
    public void disableNews(Integer id) {
        newsRepository.disableNews(id);
    }

    @Override
    public void deleteNews(News news) {
        newsRepository.delete(news);
    }

    @Override
    public void updateStatus(Integer id, Integer status) {
        newsRepository.updateStatus(id,status);
    }

    @Override
    public Optional<News> findByNewsId(Integer id) {
        return newsRepository.findById(id);
    }

    @Override
    public List<News> getlistNews() {
        return newsRepository.findAll();
    }

    @Override
    public Page<News> getAllNews(Pageable pageable) {
        return newsRepository.findAll(pageable);
    }

    @Override
    public List<News> getListNewsOrderByTime() {
        return newsRepository.getListNewsOrderByTime();
    }

    @Override
    public List<News> getNewsStudy() {
        return newsRepository.getNewsStudy();
    }

    @Override
    public Page<News> searchNews(String title, String category, Pageable pageable) {
        if(title==""){
            title= null;
        }
        if(category==""){
            category=null;
        }
        return newsRepository.searchNews(title,category,pageable);
    }

    @Override
    public long countNews() {
        return newsRepository.count();
    }

    @Override
    public List<News> getListNewsOrderByViews() {
        return newsRepository.getListNewsOrderByViews();
    }

    @Override
    public List<News> getListNewsOrderById() {
        return newsRepository.getListNewsOrderById();
    }

    @Override
    public List<News> getListNewsByCategory() {
        return null;
    }

    @Override
    public List<News> getListNewsDistinct() {
        return newsRepository.getListNewsDistinct();
    }
}

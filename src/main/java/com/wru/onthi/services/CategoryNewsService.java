package com.wru.onthi.services;

import com.wru.onthi.entity.CategoryNews;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CategoryNewsService {
    void createCategory(CategoryNews categoryNews);
    CategoryNews updateCategory(CategoryNews categoryNews);
    void deleteCategory(CategoryNews categoryNews);
    Optional<CategoryNews> findByCategoryNewsId(Integer id);
    List<CategoryNews> getlistCategoryNews();
    Page<CategoryNews> getAllCategoryNews(Pageable pageable);
    Page<CategoryNews> searchCategory(String category,Pageable pageable);
}

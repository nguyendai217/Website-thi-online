package com.wru.onthi.services;

import com.wru.onthi.entity.CategoryNews;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.Optional;

public interface CategoryNewsService {
    void createCategory(CategoryNews categoryNews);
    CategoryNews updateCategory(CategoryNews categoryNews);
    void disableCategory(Integer id);
    void deleteCategory(CategoryNews categoryNews);
    void updateCategoryStatus(Integer id, Integer status);
    Optional<CategoryNews> findByCategoryNewsId(Integer id);
    List<CategoryNews> getlistCategoryNews();
    Page<CategoryNews> getAllCategoryNews(Pageable pageable);
    Page<CategoryNews> searchCategory(String category,Pageable pageable);

}

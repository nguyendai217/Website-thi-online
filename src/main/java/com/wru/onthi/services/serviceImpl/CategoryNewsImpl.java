package com.wru.onthi.services.serviceImpl;

import com.wru.onthi.entity.CategoryNews;
import com.wru.onthi.repository.CategoryNewsRepository;
import com.wru.onthi.services.CategoryNewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryNewsImpl implements CategoryNewsService {

    @Autowired
    CategoryNewsRepository categoryNewsRepository;

    @Override
    public void createCategory(CategoryNews categoryNews) {
        categoryNewsRepository.save(categoryNews);
    }

    @Override
    public CategoryNews updateCategory(CategoryNews categoryNews) {
        return categoryNewsRepository.save(categoryNews) ;
    }

    @Override
    public void disableCategory(Integer id) {
        categoryNewsRepository.disableCategoryNews(id);
    }

    @Override
    public void deleteCategory(CategoryNews categoryNews) {
        categoryNewsRepository.delete(categoryNews);
    }

    @Override
    public void updateCategoryStatus(Integer id, Integer status) {
        categoryNewsRepository.updateStatus(id,status);
    }

    @Override
    public Optional<CategoryNews> findByCategoryNewsId(Integer id) {
        return categoryNewsRepository.findById(id);
    }

    @Override
    public List<CategoryNews> getlistCategoryNews() {
        return categoryNewsRepository.getListCategory();
    }

    @Override
    public Page<CategoryNews> getAllCategoryNews(Pageable pageable) {
        return categoryNewsRepository.findAll(pageable);
    }

    @Override
    public Page<CategoryNews> searchCategory(String category, Pageable pageable) {
        return categoryNewsRepository.searchCategory(category,pageable);
    }
}

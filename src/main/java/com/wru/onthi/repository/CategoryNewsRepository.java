package com.wru.onthi.repository;

import com.wru.onthi.entity.CategoryNews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryNewsRepository extends JpaRepository<CategoryNews, Integer> {

}

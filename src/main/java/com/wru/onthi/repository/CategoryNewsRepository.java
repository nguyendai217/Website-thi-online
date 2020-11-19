package com.wru.onthi.repository;

import com.wru.onthi.entity.CategoryNews;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryNewsRepository extends JpaRepository<CategoryNews, Integer> {

    @Query(value = "select * from CategoryNews c where c.categoryName like %:searchCategory% ", nativeQuery = true)
    Page<CategoryNews> searchCategory(@Param("searchCategory") String searchCategory, Pageable pageable);
}

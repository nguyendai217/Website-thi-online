package com.wru.onthi.repository;

import com.wru.onthi.entity.CategoryNews;
import org.apache.catalina.LifecycleState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface CategoryNewsRepository extends JpaRepository<CategoryNews, Integer> {

    @Query(value = "select * from CategoryNews c where c.categoryName like %:searchCategory% ", nativeQuery = true)
    Page<CategoryNews> searchCategory(@Param("searchCategory") String searchCategory, Pageable pageable);

    @Modifying
    @Transactional
    @Query("update CategoryNews ct set ct.status=0 where ct.id =:id")
    void disableCategoryNews(@Param("id") Integer id);

    @Modifying
    @Transactional
    @Query("update CategoryNews ct set ct.status=:status where ct.id=:id")
    void updateStatus(@Param("id") Integer id, @Param("status") Integer status);

    @Query("select c from CategoryNews c where c.status=1")
    List<CategoryNews> getListCategory();

}

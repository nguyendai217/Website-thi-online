package com.wru.onthi.repository;

import com.wru.onthi.entity.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchoolRepository extends JpaRepository<School,Integer> {
    School findBySchoolname(String name);
}

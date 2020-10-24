package com.wru.onthi.repository;

import com.wru.onthi.entity.Subject;
import com.wru.onthi.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Integer> {

    @Query(value = "SELECT * FROM subject ",nativeQuery = true)
    Page<Subject> findAllSubject(Pageable pageable);
}

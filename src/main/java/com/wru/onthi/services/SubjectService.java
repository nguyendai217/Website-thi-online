package com.wru.onthi.services;

import com.wru.onthi.entity.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.Optional;

public interface SubjectService {
    void createSubject(Subject subject);
    Subject updateSubject(Subject subject);
    void deleteSubject(Subject subject);
    void disableSubject(Integer id);
    Optional<Subject> findBySubjectId(Integer id);
    List<Subject> getlistSubject();
    Page<Subject> getAllSubject(Pageable pageable);
    Page<Subject> searchSubject(String subject,Pageable pageable);
    Subject findBySubjectCode(String code);
    List<Subject> getListSubjectByClass(Integer id);
    void updateStatus(Integer id, Integer status);
    long countSubject();
}

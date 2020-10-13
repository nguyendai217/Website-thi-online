package com.wru.onthi.services;

import com.wru.onthi.entity.Subject;

import java.util.List;
import java.util.Optional;

public interface SubjectService {
    void createSubject(Subject subject);
    Subject updateSubject(Subject subject);
    void deleteSubject(Subject subject);
    Optional<Subject> findBySubjectId(Integer id);
    List<Subject> getlistSubject();
}

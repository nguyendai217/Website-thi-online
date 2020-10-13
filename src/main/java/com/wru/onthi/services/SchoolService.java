package com.wru.onthi.services;

import com.wru.onthi.entity.School;

import java.util.List;
import java.util.Optional;

public interface SchoolService {
    void createSchool(School school);
    School updateSchool(School school);
    void deleteSchool(School school);
    List<School> getListSchool();
    Optional<School> findById(Integer id);
}

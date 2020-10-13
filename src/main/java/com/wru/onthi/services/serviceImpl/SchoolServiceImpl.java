package com.wru.onthi.services.serviceImpl;

import com.wru.onthi.entity.School;
import com.wru.onthi.repository.SchoolRepository;
import com.wru.onthi.services.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SchoolServiceImpl implements SchoolService {

    @Autowired
    SchoolRepository schoolRepository;
    @Override
    public void createSchool(School school) {
        schoolRepository.save(school);
    }

    @Override
    public School updateSchool(School school) {
        return schoolRepository.save(school);
    }

    @Override
    public void deleteSchool(School school) {
        schoolRepository.delete(school);
    }

    @Override
    public List<School> getListSchool() {
        return schoolRepository.findAll();
    }

    @Override
    public Optional<School> findById(Integer id) {
        return schoolRepository.findById(id);
    }
}

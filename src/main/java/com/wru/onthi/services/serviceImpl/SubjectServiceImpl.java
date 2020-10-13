package com.wru.onthi.services.serviceImpl;

import com.wru.onthi.entity.Subject;
import com.wru.onthi.repository.SubjectRepository;
import com.wru.onthi.services.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubjectServiceImpl implements SubjectService {

    @Autowired
    SubjectRepository subjectRepo;

    @Override
    public void createSubject(Subject subject) {
        subjectRepo.save(subject);
    }

    @Override
    public Subject updateSubject(Subject subject) {
        return subjectRepo.save(subject);
    }

    @Override
    public void deleteSubject(Subject subject) {
        subjectRepo.delete(subject);
    }

    @Override
    public Optional<Subject> findBySubjectId(Integer id) {
        return subjectRepo.findById(id);
    }

    @Override
    public List<Subject> getlistSubject() {
        return subjectRepo.findAll();
    }
}

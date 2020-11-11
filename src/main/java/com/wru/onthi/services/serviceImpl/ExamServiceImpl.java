package com.wru.onthi.services.serviceImpl;

import com.wru.onthi.entity.Exam;
import com.wru.onthi.repository.ExamRepository;
import com.wru.onthi.services.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamServiceImpl implements ExamService {

    @Autowired
    ExamRepository examRepository;

    @Override
    public List<Exam> getListExam() {
        return examRepository.findAll();
    }
}

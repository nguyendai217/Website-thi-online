package com.wru.onthi.services.serviceImpl;

import com.wru.onthi.entity.Exam;
import com.wru.onthi.repository.ExamRepository;
import com.wru.onthi.services.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExamServiceImpl implements ExamService {

    @Autowired
    ExamRepository examRepository;

    @Override
    public Optional<Exam> findByExamId(Integer examId) {
        return examRepository.findById(examId);
    }

    @Override
    public List<Exam> getListExam() {
        return examRepository.findAll();
    }

    @Override
    public List<Exam> getListExamOrderByViews() {
        return examRepository.getlistExamOrderByViews();
    }

    @Override
    public Page<Exam> getAllListExam(Pageable pageable) {
        return examRepository.findAll(pageable);
    }

    @Override
    public List<Exam> getListExamBySubjectAndClass(Integer subjectId, Integer classId) {
        return examRepository.getListExamBySubjectAndClass(subjectId,classId);
    }
}

package com.wru.onthi.services;

import com.wru.onthi.entity.Exam;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ExamService {
    Optional<Exam>findByExamId(Integer examId);
    List<Exam> getListExam();
    List<Exam> getListExamOrderByViews();
    Page<Exam> getAllListExam(Pageable pageable);
    List<Exam> getListExamBySubjectAndClass(Integer subjectId, Integer classId);
    Exam updateExam(Exam exam);
    void createExam(Exam exam);
    void deleteExam(Exam exam);
    Page<Exam> searchExam(String examCode, String subjectId, String classId, Pageable pageable);
    Page<Exam> getListExamByClass(Integer classId,Pageable pageable);
}

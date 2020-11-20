package com.wru.onthi.services;

import com.wru.onthi.entity.Exam;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ExamService {
    List<Exam> getListExam();
    List<Exam> getListExamOrderByViews();
    Page<Exam> getAllListExam(Pageable pageable);
    List<Exam> getListExamBySubjectAndClass(Integer subjectId, Integer classId);
}

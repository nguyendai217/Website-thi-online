package com.wru.onthi.services;

import com.wru.onthi.entity.ExamQuestion;

import java.util.List;

public interface ExamQuestionService {
    void createExamQuestion(ExamQuestion examQuestion);
    void removeQuestionFromExam(Integer questionId, Integer examId);
    List<ExamQuestion> getListExamQuestionByExamId(Integer examId);
}

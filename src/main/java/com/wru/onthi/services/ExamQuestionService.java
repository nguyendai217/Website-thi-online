package com.wru.onthi.services;

import com.wru.onthi.entity.ExamQuestion;

public interface ExamQuestionService {
    void createExamQuestion(ExamQuestion examQuestion);
    void removeQuestionFromExam(Integer questionId, Integer examId);
}
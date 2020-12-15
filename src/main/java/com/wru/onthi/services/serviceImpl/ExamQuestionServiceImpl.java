package com.wru.onthi.services.serviceImpl;

import com.wru.onthi.entity.ExamQuestion;
import com.wru.onthi.repository.ExamQuestionRepo;
import com.wru.onthi.services.ExamQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamQuestionServiceImpl implements ExamQuestionService {
    @Autowired
    ExamQuestionRepo examQuestionRepo;

    @Override
    public void createExamQuestion(ExamQuestion examQuestion) {
        examQuestionRepo.save(examQuestion);
    }

    @Override
    public void removeQuestionFromExam(Integer questionId, Integer examId) {
        examQuestionRepo.removeQuestionFromExam(questionId,examId);
    }

    @Override
    public List<ExamQuestion> getListExamQuestionByExamId(Integer examId) {
        return examQuestionRepo.listExamQuestionByExamId(examId);
    }
}

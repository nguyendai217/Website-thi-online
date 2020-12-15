package com.wru.onthi.services;

import com.wru.onthi.entity.Question;
import com.wru.onthi.entity.Subject;
import com.wru.onthi.model.QuestionModel;
import com.wru.onthi.services.serviceImpl.QuestionServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface QuestionService {
    void createQuestion(Question question);
    void deleteQuestion(Question question);
    Optional<Question> findById(Integer id);
    Question updateQuestion(Question question);
    Page<Question> getAllQuestion(Pageable pageable);
    List<Question> getListQuestionByExam(Integer examId);
    List<QuestionModel> getListQuestion(Integer examId,boolean history);
    Page<Question> getPageQuestionByExamId(Integer examId, Pageable pageable);
    Page<Question> getListAllQuestion(Pageable pageable);
    Page<Question> getPageQuestionBySubjectAndClass(Integer subjectId,Integer classId, Pageable pageable);
    List<Question> getListQuestionBySubjectAndClass(Integer subjectId,Integer classId);
}

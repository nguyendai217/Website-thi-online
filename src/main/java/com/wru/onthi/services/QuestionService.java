package com.wru.onthi.services;

import com.wru.onthi.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface QuestionService {
    void createQuestion(Question question);
    void deleteQuestion(Question question);
    Optional<Question> findById(Integer id);
    Question updateQuestion(Question question);
    Page<Question> getAllQuestion(Pageable pageable);
}

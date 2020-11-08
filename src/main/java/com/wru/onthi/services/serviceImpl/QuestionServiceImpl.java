package com.wru.onthi.services.serviceImpl;

import com.wru.onthi.entity.Question;
import com.wru.onthi.repository.QuestionRepository;
import com.wru.onthi.services.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    QuestionRepository questionRepository;

    @Override
    public void createQuestion(Question question) {
        questionRepository.save(question);
    }

    @Override
    public void deleteQuestion(Question question) {
        questionRepository.delete(question);
    }

    @Override
    public Optional<Question> findById(Integer id) {
        return questionRepository.findById(id);
    }

    @Override
    public Question updateQuestion(Question question) {
        return questionRepository.save(question);
    }

    @Override
    public Page<Question> getAllQuestion(Pageable pageable) {
        return questionRepository.findAll(pageable) ;
    }
}

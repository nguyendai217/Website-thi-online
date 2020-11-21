package com.wru.onthi.services.serviceImpl;

import com.wru.onthi.entity.Question;
import com.wru.onthi.model.QuestionModel;
import com.wru.onthi.repository.QuestionRepository;
import com.wru.onthi.services.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    QuestionRepository questionRepository;


    @PersistenceContext
    EntityManager entityManager;


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

    @Override
    public List<Question> getListQuestionByExam(Integer examId) {
        return questionRepository.getListQuestionByExamId(examId);
    }

    @Transactional
    @Override
    public List<QuestionModel> getListQuestion(Integer examId) {
        List<QuestionModel> listQuestion= new ArrayList<>();
        Query query= entityManager.createNativeQuery(
                "select qs.content,qs.ans_a,qs.ans_b,qs.ans_c,qs.ans_d,qs.ans_correct " +
                "from question qs where qs.exam_id = ? ");

        query.setParameter(1,examId);
        List<Object[]> objects = query.getResultList();

        for (Object[] record : objects) {
            QuestionModel questionModel= new QuestionModel();
            List<String> listAns= new ArrayList<>();
            questionModel.setContent((String) record[0]);
            listAns.add((String) record[1]);
            listAns.add((String) record[2]);
            listAns.add((String) record[3]);
            listAns.add((String) record[4]);
            questionModel.setAnsCorrect((String) record[5]);
            questionModel.setListAns(listAns);
            listQuestion.add(questionModel);
        }
        return listQuestion;
    }
}

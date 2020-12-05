package com.wru.onthi.repository;

import com.wru.onthi.entity.ExamQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface ExamQuestionRepo extends JpaRepository<ExamQuestion,Integer> {

    @Modifying
    @Transactional
    @Query(value = "delete from ExamQuestion eq " +
            "where eq.examQuestion.id =:examId " +
            "and eq.questionExam.id =:questionId")
    void removeQuestionFromExam(Integer questionId, Integer examId);

}

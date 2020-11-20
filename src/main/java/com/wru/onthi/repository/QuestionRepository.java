package com.wru.onthi.repository;

import com.wru.onthi.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question,Integer> {

    @Query(value = "select * from question where exam_id=?1 ",nativeQuery = true)
    List<Question> getListQuestionByExamId(Integer examId);
}

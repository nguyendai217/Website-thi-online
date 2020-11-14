package com.wru.onthi.repository;

import com.wru.onthi.entity.Exam;
import com.wru.onthi.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<Exam,Integer> {

    @Query("SELECT u from Exam  u order by u.views desc ")
    List<Exam> getlistExamOrderByViews();
}

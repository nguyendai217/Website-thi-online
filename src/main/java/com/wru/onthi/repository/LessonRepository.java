package com.wru.onthi.repository;

import com.wru.onthi.entity.Lesson;
import com.wru.onthi.model.LessonNew;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson,Integer> {
    Lesson findByLessonContent(String content);
    List<Lesson> findByLessonName(String name);


}

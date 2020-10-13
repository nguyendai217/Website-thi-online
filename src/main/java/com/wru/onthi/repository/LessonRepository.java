package com.wru.onthi.repository;

import com.wru.onthi.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonRepository extends JpaRepository<Lesson,Integer> {
    Lesson findByLessonContent(String content);
}

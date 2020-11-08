package com.wru.onthi.services;

import com.wru.onthi.entity.Lesson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface LessonService {
    void createLesson(Lesson lesson);
    Lesson updateLesson(Lesson lesson);
    void deleteLesson(Lesson lesson);
    List<Lesson> getListLesson();
    Optional<Lesson> findByLessonId(Integer id);
    List<Lesson> findByName(String name);
    Page<Lesson> getAllLesson(Pageable pageable);
}

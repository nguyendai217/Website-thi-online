package com.wru.onthi.services;

import com.wru.onthi.entity.Lesson;
import com.wru.onthi.model.LessonNew;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface LessonService {
    void createLesson(Lesson lesson);
    Lesson updateLesson(Lesson lesson);
    void disableLesson(Integer id);
    void deleteLesson(Lesson lesson);
    List<Lesson> getListLesson();
    Optional<Lesson> findByLessonId(Integer id);
    List<Lesson> findByName(String name);
    Page<Lesson> getAllLesson(Pageable pageable);
    List<LessonNew> getLessonNew();
    List<Lesson> getListLessonOrderById();
    List<Lesson> getListLessonOrderByViews();
    Page<Lesson> getListLessonByClassAndSubject(Integer classId, Integer subjectId,Pageable pageable);
    Lesson getContentLesson(Integer lessonId);
    Page<Lesson> searchLesson(String lessonName,String subjectId,String classId,Pageable pageable);
    void updateStatus(Integer id, Integer status);
    List<Lesson> getListLessonByClass(Integer classId);
    long countLesson();
    List<Lesson> getlessonNextOrPrev(Integer classId, Integer subjectId, Integer lessonId, Boolean next);
    List<Lesson> getListLessonDistinct();
}

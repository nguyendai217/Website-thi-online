package com.wru.onthi.repository;

import com.wru.onthi.entity.Lesson;
import com.wru.onthi.model.LessonNew;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson,Integer> {
    Lesson findByLessonContent(String content);
    List<Lesson> findByLessonName(String name);

    @Query("SELECT u from Lesson  u order by u.id desc ")
    List<Lesson> getlistLessonOrderByID();

    @Query("SELECT u from Lesson  u order by u.views desc ")
    List<Lesson> getlistLessonOrderByViews();

    @Query(value = "select ls " +
                   "from Lesson ls, Subject sb, Classroom cl " +
                   "where ls.subject.id= sb.id " +
                   "and cl.id= ls.classroom.id " +
                   "and cl.id = ?1 " +
                   "and sb.id = ?2 " , nativeQuery = false)
    List<Lesson> getListLessonByClassAndSubject(@Param("clas_Id") Integer class_Id,
                                                @Param("sub_Id")Integer subject_Id);

    @Query(value = "select ls from Lesson ls where ls.id = :lessonId",nativeQuery = false)
    Lesson getContentLesson(Integer lessonId);

}

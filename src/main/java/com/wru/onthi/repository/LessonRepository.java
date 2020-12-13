package com.wru.onthi.repository;

import com.wru.onthi.entity.Lesson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
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
                   "and sb.id = ?2 ")
    Page<Lesson> getListLessonByClassAndSubject(@Param("clas_Id") Integer class_Id,
                                                @Param("sub_Id")Integer subject_Id,Pageable pageable);

    @Query(value = "select ls from Lesson ls where ls.id = :lessonId", nativeQuery = false)
    Lesson getContentLesson(Integer lessonId);

    @Query(value = "select  * from lesson where lessonname like %:lessonName% " +
            "or subject_id like %:subjectId% or class_id like %:classId% ",nativeQuery = true)
    Page<Lesson>searchLesson(String lessonName, String subjectId, String classId, Pageable pageable);

    @Modifying
    @Transactional
    @Query("update Lesson ls set ls.status=0 where ls.id =:id")
    void disableLesson(@Param("id") Integer id);

    @Modifying
    @Transactional
    @Query("update Lesson ls set ls.status=:status where ls.id=:id")
    void updateStatus(@Param("id") Integer id, @Param("status") Integer status);

    @Query("SELECT u from Lesson  u where u.classroom.id=:classId order by u.views desc ")
    List<Lesson> getListLessonByClass(Integer classId);
}

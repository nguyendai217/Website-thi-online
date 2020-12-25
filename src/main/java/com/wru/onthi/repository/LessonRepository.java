package com.wru.onthi.repository;

import com.sun.org.apache.bcel.internal.generic.LUSHR;
import com.wru.onthi.entity.Lesson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson,Integer> {
    Lesson findByLessonContent(String content);
    List<Lesson> findByLessonName(String name);

    @Query("SELECT u from Lesson u where u.status=1 order by u.id desc ")
    List<Lesson> getlistLessonOrderByID();

    @Query("SELECT u from Lesson  u where u.status=1 order by u.views desc ")
    List<Lesson> getlistLessonOrderByViews();

    @Query(value = "select ls " +
                   "from Lesson ls, Subject sb, Classroom cl " +
                   "where ls.subject.id= sb.id " +
                   "and cl.id= ls.classroom.id " +
                   "and cl.id = ?1 " +
                   "and sb.id = ?2 "+
                   "and ls.status = 1")
    Page<Lesson> getListLessonByClassAndSubject(@Param("clas_Id") Integer class_Id,
                                                @Param("sub_Id")Integer subject_Id,Pageable pageable);

    @Query(value = "select ls from Lesson ls where ls.id = :lessonId and ls.status = 1", nativeQuery = false)
    Lesson getContentLesson(Integer lessonId);

//    @Query(value = "select  * from lesson where (lessonname like %:lessonName% " +
//            "or subject_id =:subjectId or class_id  =: classId ",nativeQuery = true)
    @Query(value = "select  * from lesson where (:lessonName IS NULL or lessonname like %:lessonName% ) " +
            "and (:subjectId IS NULL OR subject_id = :subjectId) " +
            "and (:classId IS NULL OR class_id  = :classId)",nativeQuery = true)
    Page<Lesson>searchLesson(String lessonName, String subjectId, String classId, Pageable pageable);

    @Modifying
    @Transactional
    @Query("update Lesson ls set ls.status=0 where ls.id =:id")
    void disableLesson(@Param("id") Integer id);

    @Modifying
    @Transactional
    @Query("update Lesson ls set ls.status=:status where ls.id=:id")
    void updateStatus(@Param("id") Integer id, @Param("status") Integer status);

    @Query("SELECT ls from Lesson ls where ls.classroom.id=:classId and ls.status=1 order by ls.views desc ")
    List<Lesson> getListLessonByClass(Integer classId);

    @Query(value = "select * from lesson where class_id= ?1 and subject_id = ?2 and id > ?3  ORDER BY id asc LIMIT 1",nativeQuery = true)
    List<Lesson> getLessonNext(Integer classId, Integer subjectId, Integer lessonId);

    @Query(value = "select * from lesson where class_id= ?1 and subject_id = ?2 and id < ?3  ORDER BY id desc LIMIT 1",nativeQuery = true)
    List<Lesson> getLessonPrev(Integer classId, Integer subjectId, Integer lessonId);

    @Query(value = "select distinct ls from Comment cm, Lesson ls where cm.lessonComment.id= ls.id")
    List<Lesson> getListLessonDistinct();
}

package com.wru.onthi.repository;

import com.wru.onthi.entity.Exam;
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
public interface ExamRepository extends JpaRepository<Exam,Integer> {

    @Query("SELECT u from Exam  u where u.status=1 order by u.views desc ")
    List<Exam> getlistExamOrderByViews();

    @Query("SELECT u from Exam  u where u.status=1 order by u.id desc ")
    List<Exam> getlistExamOrderByIdDesc();

    @Query(value = "select * from exam where subject_id=?1 and class_id=?2 and status= 1",nativeQuery = true)
    List<Exam> getListExamBySubjectAndClass(Integer subjectId,Integer classId);

    //@Query(value = "select  * from exam where code_exam like %:examCode% or subject_id like %:subjectId% or class_id like %:classId%",nativeQuery = true)
    @Query(value = "select  * from exam where (:examCode IS NULL or code_exam like %:examCode% ) " +
            "and (:subjectId IS NULL OR subject_id = :subjectId) " +
            "and (:classId IS NULL OR class_id  = :classId)",nativeQuery = true)
    Page<Exam> searchExam(@Param("examCode") String examCode,@Param("subjectId") String subjectId,@Param("classId") String classId, Pageable pageable);

    @Query(value = "select e from Exam e where e.exam_classroom.id =:classId and e.status=1 ")
    Page<Exam> getListExamByClass(@Param("classId") Integer classId,Pageable pageable);

    @Modifying
    @Transactional
    @Query("update Exam ex set ex.status=0 where ex.id =:examId")
    void disableExam(@Param("examId") Integer examId);

    @Modifying
    @Transactional
    @Query("update Exam ex set ex.status=:status where ex.id=:examId")
    void updateStatus(@Param("examId") Integer examId, @Param("status") Integer status);

    @Query("SELECT u from Exam  u where u.status=1 order by u.id desc ")
    List<Exam> getlistExamNew();

    @Query("select e from Exam e where e.exam_classroom.id=:classId and e.status=1")
    List<Exam> getlistExamByClass(@Param("classId") Integer classId);
}

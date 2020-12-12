package com.wru.onthi.repository;

import com.wru.onthi.entity.Subject;
import com.wru.onthi.entity.User;
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
public interface SubjectRepository extends JpaRepository<Subject, Integer> {

    @Query(value = "SELECT * FROM subject ",nativeQuery = true)
    Page<Subject> findAllSubject(Pageable pageable);

    Subject findByCode(String code);

    @Query(value = "SELECT * FROM subject sb WHERE (sb.name LIKE %:subject%) " +
            "OR (sb.code LIKE %:subject%)",nativeQuery = true)
    Page<Subject> searchSubject(@Param("subject") String subject, Pageable pageable);

    @Query(value = "SELECT sb.* FROM subject sb ,classroom cl, subject_class sl " +
            "where sb.id= sl.subject_id " +
            "and sl.class_id= cl.id " +
            "and cl.id=?1 ",nativeQuery = true)
    List<Subject> getListSubjectByClass(Integer id);

    @Modifying
    @Transactional
    @Query("update Subject u set u.status=0 where u.id=:id")
    void deleteSubject(Integer id);

    @Modifying
    @Transactional
    @Query("update Subject u set u.status =:status where u.id=:id")
    void updateStatus(@Param("id") Integer id, @Param("status") Integer status);
}

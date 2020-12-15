package com.wru.onthi.repository;

import com.wru.onthi.entity.Classroom;
import org.hibernate.internal.build.AllowPrintStacktrace;
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
public interface ClassRoomRepository extends JpaRepository<Classroom,Integer> {
    Classroom findByCode(String code);
    Classroom findByClassname(String classname);

    @Query(value = "SELECT e from Classroom e,School s where e.school.id= s.id and s.id= :idSchool and e.status=1")
    List<Classroom> getListClassBySchool(Integer idSchool);

    @Query(value = "select * from Classroom c where c.classname like %:searchClass% or c.code like %:searchClass% ",nativeQuery = true)
    Page<Classroom> searchClass(@Param("searchClass") String searchClass, Pageable pageable);

    @Query(value = "SELECT cl.* FROM subject sb ,classroom cl, subject_class sl " +
            "where sb.id= sl.subject_id " +
            "and sl.class_id= cl.id " +
            "and sb.id=?1 and cl.status=1 ",nativeQuery = true)
    List<Classroom> getListClassBySubject(Integer subjectId);

    @Modifying
    @Transactional
    @Query("update Classroom cl set cl.status=0 where cl.id =:classId")
    void disableClass(@Param("classId") Integer classId);

    @Modifying
    @Transactional
    @Query("update Classroom cl set cl.status=:status where cl.id=:classId")
    void updateStatus(@Param("classId") Integer classId, @Param("status") Integer status);
}

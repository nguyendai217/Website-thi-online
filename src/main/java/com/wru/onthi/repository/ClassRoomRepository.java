package com.wru.onthi.repository;

import com.wru.onthi.entity.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassRoomRepository extends JpaRepository<Classroom,Integer> {
    Classroom findByCode(String code);
    Classroom findByClassname(String classname);

    @Query(value = "SELECT e from Classroom e,School s where e.school.id= s.id and s.id= :idSchool" ,
            nativeQuery = false)
    List<Classroom> getListClassBySchool(Integer idSchool);
}

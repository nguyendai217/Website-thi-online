package com.wru.onthi.services;

import com.wru.onthi.entity.Classroom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ClassroomService {
    void createClass(Classroom classroom);
    Classroom updateClass(Classroom classroom);
    void disableClass(Integer classId);
    void deleteClass(Classroom classroom);
    Optional<Classroom> findById(Integer id);
    Classroom findByClassName(String name);
    Classroom findByClassCode(String code);
    Page<Classroom> getAllClass(Pageable pageable);
    List<Classroom> getAllClassroom();
    List<Classroom> listClassBySchool(Integer idSchool);
    Page<Classroom> searchClass(String keyword,Pageable pageable);
    List<Classroom> listClassBySubject(Integer subjectId);
    void updateStatus(Integer classId, Integer status);
}

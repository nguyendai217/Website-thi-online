package com.wru.onthi.services.serviceImpl;

import com.wru.onthi.entity.Classroom;
import com.wru.onthi.repository.ClassRoomRepository;
import com.wru.onthi.services.ClassroomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClassroomServiceImpl implements ClassroomService {

    @Autowired
    ClassRoomRepository classRoomRepository;

    @Override
    public void createClass(Classroom classroom) {
        classRoomRepository.save(classroom);
    }

    @Override
    public Classroom updateClass(Classroom classroom) {
        return classRoomRepository.save(classroom);
    }

    @Override
    public void deleteClass(Classroom classroom) {
        classRoomRepository.delete(classroom);
    }

    @Override
    public Optional<Classroom> findById(Integer id) {
        return classRoomRepository.findById(id);
    }

    @Override
    public Classroom findByClassName(String name) {
        return classRoomRepository.findByClassname(name);
    }

    @Override
    public Classroom findByClassCode(String code) {
        return classRoomRepository.findByCode(code);
    }

    @Override
    public Page<Classroom> getAllClass(Pageable pageable) {
        return classRoomRepository.findAll(pageable);
    }
}

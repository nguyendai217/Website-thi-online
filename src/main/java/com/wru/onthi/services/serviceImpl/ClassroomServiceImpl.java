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

    }

    @Override
    public Classroom updateClass(Classroom classroom) {
        return null;
    }

    @Override
    public void deleteClass(Classroom classroom) {

    }

    @Override
    public Optional<Classroom> findById(Integer id) {
        return Optional.empty();
    }

    @Override
    public Classroom findByClassName(String name) {
        return null;
    }

    @Override
    public Page<Classroom> getAllClass(Pageable pageable) {
        return classRoomRepository.findAll(pageable);
    }
}

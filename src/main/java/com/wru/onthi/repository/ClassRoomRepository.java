package com.wru.onthi.repository;

import com.wru.onthi.entity.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassRoomRepository extends JpaRepository<Classroom,Integer> {

}

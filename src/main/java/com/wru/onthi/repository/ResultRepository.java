package com.wru.onthi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.wru.onthi.entity.Result;

@Repository
public interface ResultRepository extends JpaRepository<Result,Integer> {
	@Override
	Optional<Result> findById(Integer id);
	
	@Query(value = "SELECT * FROM result where created_at  >= timestamp(DATE_SUB(NOW(), INTERVAL ?1 MINUTE)) and user_id = ?2 and exam_id = ?3 and status = ?4 limit 1",
			nativeQuery = true)
	Optional<Result> checkResultExist(Integer timeout, Integer userId, Integer ExamId, Integer status);
}

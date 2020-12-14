package com.wru.onthi.repository;

import com.wru.onthi.entity.Result;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResultRepository extends JpaRepository<Result,Integer> {
	@Override
	Optional<Result> findById(Integer id);
	
	@Query(value = "SELECT * FROM result " +
			"where created_at  >= timestamp(DATE_SUB(NOW(), INTERVAL ?1 MINUTE)) " +
			"and user_id = ?2 " +
			"and exam_id = ?3 " +
			"and status = ?4 limit 1",
			nativeQuery = true)
	Optional<Result> checkResultExist(Integer timeout, Integer userId, Integer ExamId, Integer status);

	@Query(value = "select r from Result r " +
			"where r.userResult.username like :username " +
			"or r.exam_result.codeExam like :examCode ")
	Page<Result> searchResultExam(String username, String examCode, Pageable pageable);

	@Query(value = "select r from Result r  order by r.score desc")
	List<Result> getListScoreDESC();
}

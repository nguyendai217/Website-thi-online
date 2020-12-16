package com.wru.onthi.services.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.wru.onthi.entity.Result;
import com.wru.onthi.repository.ResultRepository;
import com.wru.onthi.services.ResultService;

@Service
public class ResultServiceImpl implements ResultService  {

    @Autowired
    ResultRepository resultRepository;

	@Override
	public void save(Result result) {
		resultRepository.save(result);
	}

	@Override
	public Optional<Result> findById(Integer id) {
		return resultRepository.findById(id);
	}

	@Override
	public Optional<Result> checkResultExist(Integer timeout, Integer userId, Integer ExamId, Integer status) {
		return resultRepository.checkResultExist(timeout, userId, ExamId, status);
	}

	@Override
	public Page<Result> getListResultExam(Pageable pageable) {
		return resultRepository.findAll(pageable);
	}

	@Override
	public Page<Result> searchResultExam(String username, String examCode, Pageable pageable) {
		return resultRepository.searchResultExam(username,examCode,pageable);
	}

	@Override
	public void deleteResult(Result result) {
		resultRepository.delete(result);
	}

	@Override
	public List<Result> getResultScoreDESC() {
		return resultRepository.getListScoreDESC();
	}

	@Override
	public long countResult() {
		return resultRepository.count();
	}

	@Override
	public Page<Result> getListResultExamByUserId(Integer userId, Pageable pageable) {
		return resultRepository.getListResultByUserId(userId,pageable);
	}
}

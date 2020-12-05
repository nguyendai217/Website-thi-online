package com.wru.onthi.services;

import java.security.cert.PKIXRevocationChecker.Option;
import java.util.List;
import java.util.Optional;

import com.wru.onthi.entity.Result;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ResultService {
    void save(Result result);
    Optional<Result> findById(Integer id);
    Optional<Result> checkResultExist(Integer timeout, Integer userId, Integer ExamId,Integer status);
    Page<Result> getListResultExam(Pageable pageable);
    Page<Result> searchResultExam(String username, String examCode, Pageable pageable);
    void deleteResult(Result result);
    List<Result> getResultScoreDESC();
}

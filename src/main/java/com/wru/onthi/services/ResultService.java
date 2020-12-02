package com.wru.onthi.services;

import java.security.cert.PKIXRevocationChecker.Option;
import java.util.Optional;

import com.wru.onthi.entity.Result;

public interface ResultService {
    void save(Result result);
    Optional<Result> findById(Integer id);
    Optional<Result> checkResultExist(Integer timeout, Integer userId, Integer ExamId);
}

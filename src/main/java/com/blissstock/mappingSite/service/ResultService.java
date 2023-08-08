package com.blissstock.mappingSite.service;

import com.blissstock.mappingSite.entity.TestResult;

import java.util.List;
import java.util.Optional;

public interface ResultService {
    List<TestResult> getAllResults();
    Optional<TestResult> getResultById(Long id);
    TestResult saveResult(TestResult result);
    TestResult updateResult(TestResult result);
    void deleteResult(Long id);
}


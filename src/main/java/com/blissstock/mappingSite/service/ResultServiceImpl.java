package com.blissstock.mappingSite.service;

import com.blissstock.mappingSite.entity.TestResult;
import com.blissstock.mappingSite.repository.ResultRepository;
//import com.blissstock.mappingSite.service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class ResultServiceImpl implements ResultService {

    @Autowired
    private ResultRepository resultRepository;

    @Override
    public List<TestResult> getAllResults() {
        return resultRepository.findAll();
    }

    @Override
    public Optional<TestResult> getResultById(Long id) {
        return resultRepository.findById(id);
    }

    @Override
    public TestResult saveResult(TestResult result) {
        return resultRepository.save(result);
    }

    @Override
    public TestResult updateResult(TestResult result) {
        return resultRepository.save(result);
    }

    @Override
    public void deleteResult(Long id) {
        resultRepository.deleteById(id);
    }

}


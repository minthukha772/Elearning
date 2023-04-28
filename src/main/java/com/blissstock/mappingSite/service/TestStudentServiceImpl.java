package com.blissstock.mappingSite.service;

import com.blissstock.mappingSite.entity.TestStudent ;

import com.blissstock.mappingSite.repository.TestStudentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestStudentServiceImpl implements TestStudentService {

    private final TestStudentRepository testStudentRepository;

    @Autowired
    public TestStudentServiceImpl(TestStudentRepository testStudentRepository) {
        this.testStudentRepository = testStudentRepository;
    }

    @Override
    public TestStudent createTestStudent(TestStudent testStudent) {
        return testStudentRepository.save(testStudent);
    }

    @Override
    public TestStudent updateTestStudent(Long id, TestStudent testStudent) {
        testStudent.setTestStudentId(id);
        return testStudentRepository.save(testStudent);
    }

    @Override
    public void deleteTestStudent(Long id) {
        testStudentRepository.deleteById(id);
    }

    @Override
    public List<TestStudent> getAllTestStudents() {
        return testStudentRepository.findAll();
    }
}
package com.blissstock.mappingSite.service;

import com.blissstock.mappingSite.entity.TestStudent;

import java.util.List;

public interface TestStudentService {

    TestStudent createTestStudent(TestStudent testStudent);

    TestStudent updateTestStudent(Long id, TestStudent testStudent);

    void deleteTestStudent(Long id);    

    List<TestStudent> getAllTestStudents();
}
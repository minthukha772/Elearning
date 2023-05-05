package com.blissstock.mappingSite.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blissstock.mappingSite.entity.TestStudentAnswer;

public interface TestStudentAnswerRepository extends JpaRepository<TestStudentAnswer, Long>{
    
}

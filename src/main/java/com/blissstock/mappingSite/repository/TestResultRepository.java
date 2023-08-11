package com.blissstock.mappingSite.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.blissstock.mappingSite.entity.TestResult;

public interface TestResultRepository extends JpaRepository<TestResult, Long> {
    @Query(value = "Select * from test_result where test_id = :test_id order by test_id desc", nativeQuery = true)
    public List<TestResult> getListByTestId(@Param("test_id") Long test_id);

    @Query(value = "Select * from test_result where test_id = :test_id and examinee_student_id = :user_id order by result_id desc", nativeQuery = true)
    public TestResult getResultByTestIdAndUser(@Param("test_id") Long test_id, @Param("user_id") Long user_id);
}

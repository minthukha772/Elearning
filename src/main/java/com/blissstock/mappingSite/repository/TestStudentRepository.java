package com.blissstock.mappingSite.repository;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.blissstock.mappingSite.entity.TestStudent;

@Repository
public interface TestStudentRepository extends JpaRepository<TestStudent, Long> {
    @Query(value = "Select * from test_student where test_id = :test_id order by test_id desc", nativeQuery = true)
    List <TestStudent> getResultByTestId(@Param("test_id") Long test_id);
}
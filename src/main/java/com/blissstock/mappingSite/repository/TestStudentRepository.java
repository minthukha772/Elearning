package com.blissstock.mappingSite.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.blissstock.mappingSite.entity.TestStudent;

public interface TestStudentRepository extends JpaRepository<TestStudent, Long> {
    @Query(value = "Select * from test_student where test_test_id = :test_id", nativeQuery = true)
    public List<TestStudent> getStudentByTest(@Param("test_id") Long test_id);

    @Query(value = "Select * from test_student where user_info_account_id = :id limit 1", nativeQuery = true)
    public TestStudent getStudentByID(@Param("id") Long test_id);
}

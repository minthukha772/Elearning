package com.blissstock.mappingSite.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.blissstock.mappingSite.entity.MarkedAnswer;

@Repository
public interface MarkedAnswerRepository extends JpaRepository<MarkedAnswer, Long> {
    @Query(value = "Select * from marked_answer where test_student_id = :test_student_id order by test_student_id desc", nativeQuery = true)
    MarkedAnswer getResultByTestStudentId(@Param("test_student_id") Long testStudentId);
}
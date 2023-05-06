package com.blissstock.mappingSite.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.blissstock.mappingSite.entity.TestStudentAnswer;

public interface TestStudentAnswerRepository extends JpaRepository<TestStudentAnswer, Long> {
    @Query(value = "Select * from test_student_answer where student_account_id = :student_account_id", nativeQuery = true)
    public List<TestStudentAnswer> getStudentAnswerList(@Param("student_account_id") Long student_account_id);

    @Query(value = "Select * from test_student_answer where student_account_id = :student_account_id and question_id = :question_id limit 1", nativeQuery = true)
    public TestStudentAnswer getStudentAnswer(@Param("student_account_id") Long student_account_id,
            @Param("question_id") Long question_id);

    @Query(value = "Select count(id) from test_student_answer where marked_status = 'MARKING'", nativeQuery = true)
    public Integer getMarkingQuestionCount();
}

package com.blissstock.mappingSite.repository;

import com.blissstock.mappingSite.entity.TestQuestion;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TestQuestionRepository extends JpaRepository<TestQuestion, Long> {

    @Query(value = "Select * from test_question where test_id = :test_id order by question_id asc", nativeQuery = true)
    public List<TestQuestion> getQuestionByTest(@Param("test_id") Long test_id);

    @Query(value = "Select * from test_question where question_id = :id limit 1", nativeQuery = true)
    public TestQuestion getQuestionByID(@Param("id") Long id);

    @Query(value = "Select count(question_id) from test_question where question_type='FREE_ANSWER' and test_id = :test_id", nativeQuery = true)
    public Integer getFreeAnswerCount(@Param("test_id") Long test_id);

    @Transactional
    @Modifying
    @Query(value = "delete from test_question where question_id = :id", nativeQuery = true)
    public Integer deleteQuestionByID(@Param("id") Long id);
}

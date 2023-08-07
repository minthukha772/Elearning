package com.blissstock.mappingSite.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.blissstock.mappingSite.entity.TestExamineeAnswer;

public interface TestExamineeAnswerRepository extends JpaRepository<TestExamineeAnswer, Long> {
        @Query(value = "Select * from test_examinee_answer where student_account_id = :student_account_id", nativeQuery = true)
        public List<TestExamineeAnswer> getStudentAnswerList(@Param("student_account_id") Long student_account_id);

        @Query(value = "Select * from test_examinee_answer where student_account_id = :student_account_id and test_id = :test_id", nativeQuery = true)
        public List<TestExamineeAnswer> getStudentAnswerListByTestAndStudent(
                        @Param("student_account_id") Long student_account_id,
                        @Param("test_id") Long test_id);

        @Query(value = "Select * from test_examinee_answer where student_account_id = :student_account_id and test_id = :test_id limit 1", nativeQuery = true)
        public TestExamineeAnswer getStudentAnswerByTestAndStudent(
                        @Param("student_account_id") Long student_account_id,
                        @Param("test_id") Long test_id);

        @Query(value = "Select count(id) from test_examinee_answer where student_account_id = :student_account_id and test_id = :test_id", nativeQuery = true)
        public Integer getCountStudentAnswerListByTestAndStudent(
                        @Param("test_id") Long test_id,
                        @Param("student_account_id") Long student_account_id);

        @Query(value = "Select * from test_examinee_answer where test_id = :test_id", nativeQuery = true)
        public List<TestExamineeAnswer> getStudentAnswerListByTest(@Param("test_id") Long test_id);

        @Query(value = "Select * from test_examinee_answer where student_account_id = :student_account_id and question_id = :question_id limit 1", nativeQuery = true)
        public TestExamineeAnswer getStudentAnswer(@Param("student_account_id") Long student_account_id,
                        @Param("question_id") Long question_id);

        @Query(value = "Select count(id) from test_examinee_answer where marked_status = 'MARKING' and test_id = :test_id", nativeQuery = true)
        public Integer getMarkingQuestionCount(@Param("test_id") Long test_id);

        @Query(value = "Select * from test_examinee_answer where id = :id limit 1", nativeQuery = true)
        public TestExamineeAnswer getStudentAnswerByID(@Param("id") Long id);

        @Query(value = "Select * from test_examinee_answer where question_id = :question_id and student_account_id = :student_account_id limit 1", nativeQuery = true)
        public TestExamineeAnswer getStudentAnswerByQuestionID(@Param("question_id") Long question_id,
                        @Param("student_account_id") Long student_account_id);

        @Query(value = "Select count(id) from test_examinee_answer where marked_status = 'MARKING' and test_id = :test_id and student_account_id = :student_account_id", nativeQuery = true)
        public Integer getUnCheckAnswerCountByTestAndStudent(@Param("test_id") Long test_id,
                        @Param("student_account_id") Long student_account_id);
}

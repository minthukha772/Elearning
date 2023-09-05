package com.blissstock.mappingSite.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.blissstock.mappingSite.entity.TestExamineeAnswer;

public interface TestExamineeAnswerRepository extends JpaRepository<TestExamineeAnswer, Long> {
        @Query(value = "Select * from test_examinee_answer where examinee_student_id = :student_account_id", nativeQuery = true)
        public List<TestExamineeAnswer> getStudentAnswerList(@Param("student_account_id") Long student_account_id);

        @Query(value = "Select * from test_examinee_answer where examinee_student_id = :student_account_id and test_id = :test_id", nativeQuery = true)
        public List<TestExamineeAnswer> getStudentAnswerListByTestAndStudent(
                        @Param("student_account_id") Long student_account_id,
                        @Param("test_id") Long test_id);

        @Query(value = "Select * from test_examinee_answer where examinee_student_id = :student_account_id and test_id = :test_id limit 1", nativeQuery = true)
        public TestExamineeAnswer getStudentAnswerByTestAndStudent(
                        @Param("student_account_id") Long student_account_id,
                        @Param("test_id") Long test_id);

        @Query(value = "Select count(examinee_answer_id) from test_examinee_answer where examinee_student_id = :student_account_id and test_id = :test_id", nativeQuery = true)
        public Integer getCountStudentAnswerListByTestAndStudent(
                        @Param("test_id") Long test_id,
                        @Param("student_account_id") Long student_account_id);

        @Query(value = "Select count(examinee_answer_id) from test_examinee_answer where examinee_guest_id = :guest_id and test_id = :test_id", nativeQuery = true)
        public Integer getCountStudentAnswerListByTestAndGuest(
                        @Param("test_id") Long test_id,
                        @Param("guest_id") Long guest_id);

        @Query(value = "Select * from test_examinee_answer where test_id = :test_id", nativeQuery = true)
        public List<TestExamineeAnswer> getStudentAnswerListByTest(@Param("test_id") Long test_id);

        @Query(value = "Select * from test_examinee_answer where examinee_student_id = :student_account_id and question_id = :question_id limit 1", nativeQuery = true)
        public TestExamineeAnswer getStudentAnswer(@Param("student_account_id") Long student_account_id,
                        @Param("question_id") Long question_id);

        @Query(value = "Select * from test_examinee_answer where examinee_guest_id = :guest_id and question_id = :question_id limit 1", nativeQuery = true)
        public TestExamineeAnswer getGuestAnswer(@Param("guest_id") Long guest_id,
                        @Param("question_id") Long question_id);

        @Query(value = "Select count(examinee_answer_id) from test_examinee_answer where marked_status = 'MARKING' and test_id = :test_id", nativeQuery = true)
        public Integer getMarkingQuestionCount(@Param("test_id") Long test_id);

        @Query(value = "Select * from test_examinee_answer where id = :id limit 1", nativeQuery = true)
        public TestExamineeAnswer getStudentAnswerByID(@Param("id") Long id);

        @Query(value = "Select * from test_examinee_answer where question_id = :question_id and examinee_student_id = :student_account_id limit 1", nativeQuery = true)
        public TestExamineeAnswer getStudentAnswerByQuestionID(@Param("question_id") Long question_id,
                        @Param("student_account_id") Long student_account_id);
         
        @Query(value = "Select * from test_examinee_answer where question_id = :question_id and examinee_guest_id = :guestUser_account_id limit 1", nativeQuery = true)
        public TestExamineeAnswer getGuestUserAnswerByQuestionID(@Param("question_id") Long question_id,
                        @Param("guestUser_account_id") Long guestUser_account_id);

        @Query(value = "Select count(examinee_answer_id) from test_examinee_answer where marked_status = 'MARKING' and test_id = :test_id and examinee_student_id = :student_account_id", nativeQuery = true)
        public Integer getUnCheckAnswerCountByTestAndStudent(@Param("test_id") Long test_id,
                        @Param("student_account_id") Long student_account_id);

        @Query(value = "Select * from test_examinee_answer where examinee_guest_id = :guest_account_id and test_id = :test_id limit 1", nativeQuery = true)
        public TestExamineeAnswer getStudentAnswerByTestAndGuest(
                                        @Param("guest_account_id") Long guest_account_id,
                                        @Param("test_id") Long test_id);

        @Query(value = "Select count(examinee_answer_id) from test_examinee_answer where marked_status = 'MARKING' and test_id = :test_id and examinee_guest_id = :examinee_guest_id", nativeQuery = true)
        public Integer getUnCheckAnswerCountByTestAndGuest(@Param("test_id") Long test_id,
                        @Param("examinee_guest_id") Long examinee_guest_id);
}

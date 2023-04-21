package com.blissstock.mappingSite.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.blissstock.mappingSite.entity.Exam;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {

    /*
     * @Query(value = "SELECT * FROM exam ORDER BY exam_date ASC", nativeQuery =
     * true)
     * public List<Exam> findAllByDate();
     */

    /*
     * @Query(value =
     * "SELECT * FROM exam JOIN user_account ON exam.user_id = user_account.account_id where exam.user_id=10010"
     * , nativeQuery = true)
     * public List<Exam> findExamByUserId();
     */

    @Query(value = "SELECT * FROM test_student JOIN user_account ON test_student.user_id = user_account.account_id JOIN exam ON test_student.exam_id = exam.id where test_student.user_id=:userId ORDER BY exam.exam_date ASC", nativeQuery = true)
    public List<Exam> findExamByUserId(@Param("userId") Long userId);

    @Query(value = "SELECT COUNT(*) AS num_rows FROM test_student where test_student.user_id=:userId", nativeQuery = true)
    public Long count2(@Param("userId") Long userId2);

}

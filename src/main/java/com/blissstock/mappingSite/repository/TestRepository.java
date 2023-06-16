package com.blissstock.mappingSite.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.blissstock.mappingSite.entity.Test;

public interface TestRepository extends JpaRepository<Test, Long> {

        /**
         *
         * @param exam_status
         * @return
         */
        @Query(value = "Select * from test where exam_status = :exam_status and user_info_account_id = :user_id order by test_id desc", nativeQuery = true)
        public List<Test> getListByStatusAndUser(@Param("exam_status") String exam_status,
                        @Param("user_id") Long user_id);

        @Query(value = "Select * from test where user_info_account_id = :user_id and is_delete = 'true' order by test_id desc", nativeQuery = true)
        public List<Test> getDeletedListByUser(@Param("user_id") Long user_id);                

        @Query(value = "Select * from test where course_info_course_id = :course_id and user_info_account_id = :user_id order by test_id desc", nativeQuery = true)
        public List<Test> getListByCourseAndUser(@Param("course_id") Long course_id, @Param("user_id") Long user_id);

        @Query(value = "Select * from test where date >= :fromDate and date <= :toDate and user_info_account_id = :user_id order by test_id desc", nativeQuery = true)
        public List<Test> getListByDateAndUser(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate,
                        @Param("user_id") Long user_id);

        //@Query(value = "Select * from test where user_info_account_id = :user_id order by test_id desc", nativeQuery = true)
        @Query(value = "SELECT * FROM test WHERE user_info_account_id = :user_id and is_delete <> 'true' ORDER BY test_id DESC", nativeQuery = true)
        public List<Test> getListByUser(@Param("user_id") Long user_id);

        //@Query(value = "Select * from test, test_student where test.test_id = test_student.test_test_id and test_student.user_info_account_id = :user_id order by test_id desc", nativeQuery = true)
        @Query(value = "SELECT * FROM test, test_student WHERE test.test_id = test_student.test_test_id and test_student.user_info_account_id = :user_id and is_delete <> 'true' ORDER BY test_id DESC", nativeQuery = true)
        public List<Test> getListByStudent(@Param("user_id") Long user_id);

        //@Query(value = "Select * from test order by test_id desc", nativeQuery = true)
        @Query(value = "SELECT * FROM test WHERE is_delete <> 'true' ORDER BY test_id DESC", nativeQuery = true)
        public List<Test> getListByAdmin();

        @Query(value = "SELECT * FROM test WHERE is_delete = 'true' ORDER BY test_id DESC", nativeQuery = true)
        public List<Test> getDeletedListByAdmin();

        @Query(value = "Select * from test where exam_status = :exam_status order by test_id desc", nativeQuery = true)
        public List<Test> getListByStatus(@Param("exam_status") String exam_status);

        @Query(value = "Select * from test where course_info_course_id = :course_id order by test_id desc", nativeQuery = true)
        public List<Test> getListByCourse(@Param("course_id") Long course_id);

        @Query(value = "Select * from test where date >= :fromDate and date <= :toDate order by test_id desc", nativeQuery = true)
        public List<Test> getListByDate(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

        @Query(value = "Select * from test where test_id = :test_id limit 1", nativeQuery = true)
        public Test getTestByID(@Param("test_id") Long test_id);

}

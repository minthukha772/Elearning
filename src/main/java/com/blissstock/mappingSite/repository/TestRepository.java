package com.blissstock.mappingSite.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.blissstock.mappingSite.entity.Test;

public interface TestRepository extends JpaRepository<Test, Long> {

        /**
         *
         * @param exam_status
         * @return
         */
        @Query(value = "Select * from test where exam_status = :exam_status and user_id = :user_id order by test_id desc", nativeQuery = true)
        public List<Test> getListByStatusAndUser(@Param("exam_status") String exam_status,
                        @Param("user_id") Long user_id);

        @Query(value = "SELECT * FROM test, test_examinee WHERE exam_status = :exam_status and test.test_id = test_examinee.test_id and test_examinee.examinee_student_id = :user_id and is_delete <> 'true' ORDER BY test_id DESC", nativeQuery = true)
        public List<Test> getListByStatusAndStudentId(@Param("exam_status") String exam_status,
                        @Param("user_id") Long user_id);

        @Query(value = "Select * from test where user_id = :user_id and is_delete = 'true' order by test_id desc", nativeQuery = true)
        public List<Test> getDeletedListByUser(@Param("user_id") Long user_id);

        @Query(value = "Select * from test where course_id = :course_id and user_id = :user_id order by test_id desc", nativeQuery = true)
        public List<Test> getListByCourseAndUser(@Param("course_id") Long course_id, @Param("user_id") Long user_id);

        @Query(value = "Select * from test where date BETWEEN :fromDate and :toDate and user_id = :user_id order by test_id desc", nativeQuery = true)
        public List<Test> getListByDateAndUser(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate,
                        @Param("user_id") Long user_id);


        @Query(value = "Select * from test, test_examinee where date >= :fromDate and date <= :toDate and test.test_id = test_examinee.test_id and test_examinee.examinee_student_id = :user_id order by test_id desc", nativeQuery = true)
        public List<Test> getListByDateAndStudentId(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate,
                        @Param("user_id") Long user_id);

        // @Query(value = "Select * from test where user_id = :user_id
        // order by test_id desc", nativeQuery = true)
        @Query(value = "SELECT * FROM test WHERE user_id = :user_id and is_delete <> 'true' ORDER BY test_id DESC", nativeQuery = true)
        public List<Test> getListByUser(@Param("user_id") Long user_id);

        // @Query(value = "Select * from test, test_examinee where test.test_id =
        // test_examinee.test_id and test_examinee.examinee_student_id = :user_id
        // order by test_id desc", nativeQuery = true)
        @Query(value = "SELECT * FROM test, test_examinee WHERE test.test_id = test_examinee.test_id and test_examinee.examinee_student_id = :user_id and test.is_delete <> 'true' ORDER BY test.test_id DESC", nativeQuery = true)
        public List<Test> getListByStudent(@Param("user_id") Long user_id);

        // @Query(value = "Select * from test order by test_id desc", nativeQuery =
        // true)
        @Query(value = "SELECT * FROM test WHERE is_delete <> 'true' ORDER BY test_id DESC", nativeQuery = true)
        public List<Test> getListByAdmin();

        @Query(value = "SELECT * FROM test WHERE is_delete = 'true' ORDER BY test_id DESC", nativeQuery = true)
        public List<Test> getDeletedListByAdmin();

        @Query(value = "Select * from test where exam_status = :exam_status order by test_id desc", nativeQuery = true)
        public List<Test> getListByStatus(@Param("exam_status") String exam_status);

        @Query(value = "Select * from test where course_id = :course_id order by test_id desc", nativeQuery = true)
        public List<Test> getListByCourse(@Param("course_id") Long course_id);

        @Query(value = "Select * from test where date BETWEEN :fromDate and :toDate order by test_id desc", nativeQuery = true)
        public List<Test> getListByDate(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate);
        
        @Query(value = "Select * from test where test_id = :test_id limit 1", nativeQuery = true)
        public Test getTestByID(@Param("test_id") Long test_id);

        //  public void insertTest(String description, String student_guest, String section_name, int minutes_allowed,
        //                  int passing_score, Date examDate, String exam_start_time, String exam_end_time,
        //                  String exam_status, String exam_start_time2, String exam_end_time2, int i, Object object);

        //  @Modifying
        //  @Query(value = "INSERT IGNORE INTO test (  user_id, course_id,description, student_guest, section_name, minutes_allowed, passing_score_percent, date, start_time, end_time, exam_status, is_delete, deleted_at, exam_target, exam_announce) VALUES (1, 1, :description, :student_guest, :section_name, :minutes_allowed, :passing_score_percent, :date, :start_time, :end_time, :exam_status, :is_delete, :deleted_at, :exam_target, :exam_announce)", nativeQuery = true)
        //  void insertTest( @Param("description") String description, @Param("student_guest") String student_guest, @Param("section_name") String section_name, @Param("minutes_allowed") Integer minutes_allowed, @Param("passing_score_percent") Integer passing_score_percent, @Param("date") Date date, @Param("start_time") String start_time, @Param("end_time") String end_time, @Param("exam_status") String exam_status, @Param("is_delete") String is_delete, @Param("deleted_at") String deleted_at, @Param("exam_target") Integer exam_target, @Param("exam_announce") Integer exam_announce);
}


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

        @Query(value = "Select * from test where course_info_course_id = :course_id and user_info_account_id = :user_id order by test_id desc", nativeQuery = true)
        public List<Test> getListByCourseAndUser(@Param("course_id") Long course_id, @Param("user_id") Long user_id);

        @Query(value = "Select * from test where date >= :fromDate and date <= :toDate and user_info_account_id = :user_id order by test_id desc", nativeQuery = true)
        public List<Test> getListByDateAndUser(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate, @Param("user_id") Long user_id);

        @Query(value = "Select * from test where user_info_account_id = :user_id order by test_id desc", nativeQuery = true)
        public List<Test> getListByUser(@Param("user_id") Long user_id);

        @Query(value = "insert into test(date, description, end_time, exam_status, minutes_allowed, passing_score_percent,"
                        +
                        "section_name, start_time, course_info_course_id, user_info_account_id) values(:date, :description, :end_time,"
                        +
                        " :exam_status, :minutes_allowed, :passing_score_percent, :section_name, :start_time, :course_id, :user_id)", nativeQuery = true)
        public Void insertTest(@Param("date") String date, @Param("description") String description,
                        @Param("end_time") String end_time,
                        @Param("exam_status") String exam_status, @Param("minutes_allowed") Integer minutes_allowed,
                        @Param("passing_score_percent") Integer passing_score_percent,
                        @Param("section_name") String section_name, @Param("start_time") String start_time,
                        @Param("course_id") Integer course_id, @Param("user_id") Long user_id);
}

package com.blissstock.mappingSite.repository;

import java.util.List;

import com.blissstock.mappingSite.entity.CourseInfo;
import com.blissstock.mappingSite.entity.Review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseInfoRepository extends JpaRepository<CourseInfo, Long> {
    @Query(nativeQuery = true, value="select * from course_info where first_name ilike %:courseName%")
	public List<CourseInfo> findByCourseName(@Param("courseName")String courseName);

    // @Query(value = "SELECT * FROM join_course_user WHERE course_id=50004;",nativeQuery = true)
    // public List<CourseInfo> findByCourseI();
}
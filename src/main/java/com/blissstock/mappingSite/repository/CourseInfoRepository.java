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

    @Query(value = "SELECT * FROM course_info WHERE course_id=:courseID AND uid_fkey=:uid",nativeQuery = true)
    public CourseInfo findByCourseIDUID(@Param("courseID") Long courseID,@Param("uid") Long uid);

    @Query(value = "UPDATE course_info SET course_name=:courseName, category=:category, class_link=:classLink, level=:level, about_course=:aboutCourse, prerequisite=:prerequisite, course_fees=:fees, is_course_approved=:isCourseApproved WHERE course_id=:courseID",nativeQuery = true)
    public CourseInfo updateVideoCourse(@Param("courseID") Long courseID,@Param("courseName")String courseName, @Param("category")String category, @Param("classLink")String classLink, @Param("level")String level, @Param("aboutCourse")String aboutCourse, @Param("prerequisite")String prerequisite, @Param("fees")int fees, @Param("isCourseApproved")boolean isCourseApproved);

    // @Query(value = "SELECT c.course_name, u.user_name FROM course_info c,user_info u where course_id=50004 and c.uid_fkey = u.account_id",nativeQuery = true)
    // public 
}
package com.blissstock.mappingSite.repository;

import com.blissstock.mappingSite.entity.CourseInfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Date;

@Repository
public interface CourseRepository extends JpaRepository<CourseInfo, Long> {
    // @Query(nativeQuery = true, value="select * from course_info where course_name=:courseName")
	// public CourseInfo findByCourseName(@Param("courseName") String courseName);

    // String query = "select * from course_info cInfo, user_info uInfo where ";
    // @Query(value=query, nativeQuery = true)
    List<CourseInfo> findByCourseName(String courseName);
    List<CourseInfo> findByStartDate(Date startDate);
    //List<CourseInfo> findByUserInfo(UserInfo userInfo);
    List<CourseInfo> findByLevel(String level);
    List<CourseInfo> findByCategory(String category);
    List<CourseInfo> findByClassType(String classType);
    List<CourseInfo> findByLevelAndCategory(String level, String category);
    List<CourseInfo> findByLevelAndClassType(String level, String classType);
    List<CourseInfo> findByCategoryAndClassType(String category, String classType);
    List<CourseInfo> findByLevelAndCategoryAndClassType(String level, String category, String classType);
   

    // @Query(nativeQuery = true, value = "select * from course_info ci inner join ci.user_info ui where ci.course_name=:courseName and ui in (:user_info)")
    // List<CourseInfo> findByUserInfo(@Param("courseName") String courseName, @Param("user_info") List<UserInfo> userInfo);
}

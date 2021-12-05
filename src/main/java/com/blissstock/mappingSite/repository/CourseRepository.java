package com.blissstock.mappingSite.repository;

import com.blissstock.mappingSite.entity.CourseInfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<CourseInfo, Long>{
    // @Query(value = "SELECT uid FROM join_course_user WHERE course_id=50004;",nativeQuery = true)
    // public List<CourseInfo> findByCourseI();
}

package com.blissstock.mappingSite.repository;

import java.util.List;

import com.blissstock.mappingSite.dto.CourseInfoDTO;
import com.blissstock.mappingSite.entity.CourseInfo;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseInfoRepository extends JpaRepository<CourseInfo, Long> {
    @Query(nativeQuery = true, value="select * from course_info where first_name like %:courseName%")
	public List<CourseInfo> findByCourseName(@Param("courseName")String courseName);

    public List<CourseInfo> findAll(Specification<CourseInfo> spec, Pageable pageable);


}
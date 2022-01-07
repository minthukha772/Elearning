package com.blissstock.mappingSite.repository;

import java.util.Date;
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

    public List<CourseInfo> findByClassType(String classType);
    public List<CourseInfo> findAll(Specification<CourseInfo> spec, Pageable pageable);

        
	// public List<CourseInfo> findByCourseName(String courseName);
    // public List<CourseInfo> findByStartDate(Date startDate);
    // //List<CourseInfo> findByUserInfo(UserInfo userInfo);
    // public List<CourseInfo> findByLevel(String level);
    // public List<CourseInfo> findByCategory(String category);
   
    // public List<CourseInfo> findByLevelAndCategory(String level, String category);
    // public List<CourseInfo> findByLevelAndClassType(String level, String classType);
    // public List<CourseInfo> findByCategoryAndClassType(String category, String classType);
    // public List<CourseInfo> findByLevelAndCategoryAndClassType(String level, String category, String classType);


}
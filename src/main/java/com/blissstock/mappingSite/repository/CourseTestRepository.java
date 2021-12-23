package com.blissstock.mappingSite.repository;

import java.util.List;

import com.blissstock.mappingSite.entity.CourseInfo;
import com.blissstock.mappingSite.entity.CourseInfoTest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseTestRepository extends JpaRepository<CourseInfoTest, Long> {
    
}
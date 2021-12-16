package com.blissstock.mappingSite.repository;


import com.blissstock.mappingSite.entity.CourseTesting;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseTestingRepository extends JpaRepository<CourseTesting, Long> {
    
}
package com.blissstock.mappingSite.specification;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;

import com.blissstock.mappingSite.dto.CourseInfoDTO;
import com.blissstock.mappingSite.entity.CourseInfo;
import com.blissstock.mappingSite.entity.UserInfo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class CourseSpecification {


    public Specification<CourseInfo> getCourses(CourseInfoDTO courseInfoDTO) {
        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            //By course name
            if (courseInfoDTO.getCourseName() != null && !courseInfoDTO.getCourseName().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("courseName")),
                "%" + courseInfoDTO.getCourseName().toLowerCase() + "%"));
            }

            if (courseInfoDTO.getTeacherName() != null && !courseInfoDTO.getTeacherName().isEmpty()) {
               
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("userInfo").get("userName")),
                "%" + courseInfoDTO.getTeacherName().toLowerCase() + "%"));
            }
                
            //By start_date
            if (courseInfoDTO.getStartDate() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("startDate"), courseInfoDTO.getStartDate()));
            }

            //By end_date
            if( courseInfoDTO.getEndDate() != null){
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("endDate"), courseInfoDTO.getEndDate()));
            }

            //Only get approved course
            predicates.add(criteriaBuilder.equal(root.get("isCourseApproved"), true));
            query.orderBy(criteriaBuilder.desc(root.get("startDate")));
        
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
    
}

package com.blissstock.mappingSite.service;

import java.util.List;

import com.blissstock.mappingSite.dto.CourseDTO;
import com.blissstock.mappingSite.dto.CourseInfoDTO;
import com.blissstock.mappingSite.entity.CourseInfo;
import com.blissstock.mappingSite.repository.CourseInfoRepository;
import com.blissstock.mappingSite.specification.CourseSpecification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CourseServiceImpl implements CourseService{

    @Autowired
    CourseInfoRepository courseInfoRepository;

    @Autowired
    CourseSpecification courseSpecification;

    //TODO Get from application.properties
    Integer DEFAULT_COURSE_LIMIT = 1400;

    @Override
    public List<CourseInfo> getCourseList(CourseInfoDTO courseInfoDTO) {
        Pageable paging = PageRequest.of(0,DEFAULT_COURSE_LIMIT);
        return courseInfoRepository.findAll(courseSpecification.getCourses(courseInfoDTO), paging);
    }
    
}

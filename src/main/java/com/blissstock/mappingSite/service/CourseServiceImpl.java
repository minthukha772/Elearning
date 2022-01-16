package com.blissstock.mappingSite.service;

import java.util.List;

import com.blissstock.mappingSite.dto.CourseInfoDTO;
import com.blissstock.mappingSite.entity.CourseInfo;
import com.blissstock.mappingSite.repository.CourseInfoRepository;
import com.blissstock.mappingSite.specification.CourseSpecification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CourseServiceImpl implements CourseService{

    private static Logger logger = LoggerFactory.getLogger(CourseServiceImpl.class);


    @Autowired
    CourseInfoRepository courseInfoRepository;

    @Autowired
    CourseSpecification courseSpecification;

    //TODO Get from application.properties
    @Value("${com.blissstock.mapping-site.config.courseSearchLimit}")
    Integer DEFAULT_COURSE_LIMIT = 0;

    @Override
    public List<CourseInfo> getCourseList(CourseInfoDTO courseInfoDTO) {
        logger.debug("DEFAULT_COURSE_LIMIT: {}",DEFAULT_COURSE_LIMIT);
        Pageable paging = PageRequest.of(0,DEFAULT_COURSE_LIMIT);
        return courseInfoRepository.findAll(courseSpecification.getCourses(courseInfoDTO), paging);
    }
    
}

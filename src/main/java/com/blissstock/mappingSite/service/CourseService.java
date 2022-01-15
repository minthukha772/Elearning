package com.blissstock.mappingSite.service;

import java.util.List;

import com.blissstock.mappingSite.dto.CourseInfoDTO;
import com.blissstock.mappingSite.entity.CourseInfo;

public interface CourseService {
    
    public List<CourseInfo> getCourseList(CourseInfoDTO courseInfoDTO);
}

package com.blissstock.mappingSite.service;

import java.util.List;

import com.blissstock.mappingSite.dto.CourseInfoDTO;
import com.blissstock.mappingSite.entity.CourseInfo;
import com.blissstock.mappingSite.exceptions.CourseNotFoundException;
import com.blissstock.mappingSite.exceptions.SyllabusNotFoundException;

public interface CourseService {
    
    public List<CourseInfo> getCourseList(CourseInfoDTO courseInfoDTO);

    public void deleteCourseInfo(CourseInfo courseInfo);

    public void verifyCourseInfo(CourseInfo courseInfo);
}

package com.blissstock.mappingSite.service;

import com.blissstock.mappingSite.dto.TeacherReviewDTO;
import com.blissstock.mappingSite.dto.UserRegisterDTO;
import com.blissstock.mappingSite.entity.CourseInfo;
import com.blissstock.mappingSite.entity.ReviewTest;

public interface TeacherReviewService {
  public void addReview(TeacherReviewDTO trReviewDTO, CourseInfo course);
  //public UserRegisterDTO getUserByID(Long id);
}

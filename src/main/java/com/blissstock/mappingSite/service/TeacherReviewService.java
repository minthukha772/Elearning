package com.blissstock.mappingSite.service;

import com.blissstock.mappingSite.dto.TeacherReviewDTO;
import com.blissstock.mappingSite.dto.UserRegisterDTO;
import com.blissstock.mappingSite.entity.CourseInfo;
import com.blissstock.mappingSite.entity.ReviewTest;
import com.blissstock.mappingSite.entity.UserInfo;

public interface TeacherReviewService {
  public void addReview(TeacherReviewDTO trReviewDTO, CourseInfo course, UserInfo userInfo);
  //public UserRegisterDTO getUserByID(Long id);
}

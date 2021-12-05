package com.blissstock.mappingSite.service;

import com.blissstock.mappingSite.dto.StudentReviewDTO;
import com.blissstock.mappingSite.dto.UserRegisterDTO;
import com.blissstock.mappingSite.entity.CourseInfo;
import com.blissstock.mappingSite.entity.ReviewTest;
import com.blissstock.mappingSite.entity.UserInfo;

public interface StudentReviewService {
  public void addReview(StudentReviewDTO stuReviewDTO, CourseInfo course, UserInfo userInfo);
  //public UserRegisterDTO getUserByID(Long id);
}

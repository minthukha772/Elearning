package com.blissstock.mappingSite.service;

import com.blissstock.mappingSite.dto.StudentReviewDTO;
import com.blissstock.mappingSite.dto.UserRegisterDTO;
import com.blissstock.mappingSite.entity.CourseInfo;
import com.blissstock.mappingSite.entity.ReviewTest;

public interface StudentReviewService {
  public void addReview(StudentReviewDTO stuReviewDTO, CourseInfo course);
  //public UserRegisterDTO getUserByID(Long id);
}

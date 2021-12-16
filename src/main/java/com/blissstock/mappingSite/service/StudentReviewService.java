package com.blissstock.mappingSite.service;

import com.blissstock.mappingSite.dto.StudentReviewDTO;
import com.blissstock.mappingSite.entity.CourseInfo;
import com.blissstock.mappingSite.entity.UserInfo;

public interface StudentReviewService {
  public void addReview(StudentReviewDTO stuReviewDTO, Long courseId, Long uid);
  //public UserRegisterDTO getUserByID(Long id);
}

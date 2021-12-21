package com.blissstock.mappingSite.service;

import com.blissstock.mappingSite.dto.TeacherReviewDTO;
import com.blissstock.mappingSite.dto.UserRegisterDTO;
import com.blissstock.mappingSite.entity.CourseInfo;
import com.blissstock.mappingSite.entity.ReviewTest;
import com.blissstock.mappingSite.entity.UserInfo;

public interface TeacherReviewService {
  public void addReview(TeacherReviewDTO trReviewDTO, Long courseId, Long userId);
  //public UserRegisterDTO getUserByID(Long id);
}

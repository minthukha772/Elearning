package com.blissstock.mappingSite.service;

import com.blissstock.mappingSite.dto.TeacherReviewDTO;

public interface TeacherReviewService {
  public void addReview(TeacherReviewDTO trReviewDTO, Long courseId, Long userId);
  //public UserRegisterDTO getUserByID(Long id);
}

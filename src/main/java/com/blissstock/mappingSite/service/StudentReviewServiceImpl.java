/* package com.blissstock.mappingSite.service;
package com.blissstock.mappingSite.service;

import javax.transaction.Transactional;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.blissstock.mappingSite.dto.StudentReviewDTO;
import com.blissstock.mappingSite.entity.CourseInfo;
import com.blissstock.mappingSite.entity.Review;
import com.blissstock.mappingSite.entity.ReviewTest;
import com.blissstock.mappingSite.entity.UserInfo;
import com.blissstock.mappingSite.repository.ReviewRepository;
import com.blissstock.mappingSite.service.StudentReviewService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class StudentReviewServiceImpl implements StudentReviewService {


  @Autowired
  private ReviewRepository reviewRepo;

  public void addReview(StudentReviewDTO studentReviewDTO, CourseInfo course, UserInfo userInfo) {
    
    Review review = Review.fromReviewDTO(studentReviewDTO);
    //System.out.println(review.getFeedback());
    review.setJoin(join);
    review.setCourseInfo(course);
    review.setUserInfo(userInfo);

    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();
    validator.validate(review).forEach(e -> System.out.println(e.getMessage()));
    reviewRepo.save(review);
  }

  

  // @Override
  // public UserRegisterDTO getUserByID(Long id) {
  //   //TODO to Implement
  //   return new UserRegisterDTO();
  // }
}
 */

package com.blissstock.mappingSite.utils;

import com.blissstock.mappingSite.dto.EmailDTO;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.Email;
import org.hibernate.validator.HibernateValidator;

public class CheckEmail {

  //if email is not validate throw ConstraintViolationException exception
  public static void check(String email) throws ConstraintViolationException {
    Validator validator = Validation
      .byProvider(HibernateValidator.class)
      .configure()
      .buildValidatorFactory()
      .getValidator();

    Set<ConstraintViolation<EmailDTO>> constraintViolation = validator.validate(
      new EmailDTO(email)
    );
    boolean isValid = constraintViolation.size() == 0;

    if (!isValid) {
      throw new ConstraintViolationException(
        "email is not valid",
        constraintViolation
      );
    }
  }
}

package com.blissstock.mappingSite.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import com.blissstock.mappingSite.dto.UserRegisterDTO;
import com.blissstock.mappingSite.validation.ConstrainMessage;
import com.github.javafaker.Faker;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class UserRegisterValidatorTest {

  private static Validator validator;
  private UserRegisterDTO userTemplate;
  private static Faker faker;

  @BeforeAll
  public static void setUpValidator() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
    faker = new Faker();
  }

  @BeforeEach
  public void setUpUserTemplate() {
    userTemplate = new UserRegisterDTO();
    userTemplate.setEmail(faker.bothify("????##@gmail.com"));
    userTemplate.setName(faker.name().fullName());
    userTemplate.setPassword(faker.bothify("????####"));
    userTemplate.setConfirmPassword(userTemplate.getPassword());
    userTemplate.setGender("Male");
    userTemplate.setPhone(faker.phoneNumber().phoneNumber());
    userTemplate.setDob(faker.date().birthday(13, 60));
    userTemplate.setZipCode(faker.number().numberBetween(0, 99999));
    userTemplate.setCity(faker.address().city());
    userTemplate.setDivision(faker.address().state());
    userTemplate.setAddress(faker.address().fullAddress());
    userTemplate.setEducation(faker.educator().university());
    userTemplate.setAcceptTerm(true);
  }

  @Test
  @Tag("validTest")
  public void validTest() {
    Set<ConstraintViolation<UserRegisterDTO>> constraintViolations = validator.validate(
      userTemplate
    );
    assertThat(constraintViolations).hasSize(0);
  }

  @Test
  @Tag("emptyEmail")
  public void emptyEmailTest() {
    userTemplate.setEmail("");
    Set<ConstraintViolation<UserRegisterDTO>> constraintViolations = validator.validate(
      userTemplate
    );

    assertThat(constraintViolations)
      .hasSize(1)
      .extracting(c -> c.getPropertyPath().toString(), c -> c.getMessage())
      .contains(
        tuple("email", ConstrainMessage.INVALID_EMAIL_FORMAT_CONSTRAIN_MESSAGE)
      );
  }

  @Test
  @Tag("invalidEmail")
  public void invalidEmailTest() {
    userTemplate.setEmail("bsbsbs");
    Set<ConstraintViolation<UserRegisterDTO>> constraintViolations = validator.validate(
      userTemplate
    );
    assertThat(constraintViolations)
      .hasSize(1)
      .extracting(c -> c.getPropertyPath().toString(), c -> c.getMessage())
      .contains(
        tuple("email", ConstrainMessage.INVALID_EMAIL_FORMAT_CONSTRAIN_MESSAGE)
      );
  }

  @Test
  @Tag("emptyPassword")
  public void emptyPasswordTest() {
    userTemplate.setPassword("");
    Set<ConstraintViolation<UserRegisterDTO>> constraintViolations = validator.validate(
      userTemplate
    );

    assertThat(constraintViolations)
      .hasSize(3)
      .extracting(c -> c.getPropertyPath().toString(), c -> c.getMessage())
      .contains(
        tuple("password", ConstrainMessage.PASSWORD_CONSTRAIN_MESSAGE),
        tuple("password", ConstrainMessage.PASSWORD_LENGTH_CONSTRAIN_MESSAGE),
        tuple(
          "confirmPassword",
          ConstrainMessage.PASSWORD_MATCH_CONSTRAIN_MESSAGE
        )
      );
  }

  @Test
  @Tag("emptyConfirmPassword")
  public void emptyConfirmPasswordTest() {
    userTemplate.setConfirmPassword("");
    Set<ConstraintViolation<UserRegisterDTO>> constraintViolations = validator.validate(
      userTemplate
    );

    assertThat(constraintViolations)
      .hasSize(3)
      .extracting(c -> c.getPropertyPath().toString(), c -> c.getMessage())
      .contains(
        tuple("confirmPassword", ConstrainMessage.PASSWORD_CONSTRAIN_MESSAGE),
        tuple(
          "confirmPassword",
          ConstrainMessage.PASSWORD_LENGTH_CONSTRAIN_MESSAGE
        ),
        tuple(
          "confirmPassword",
          ConstrainMessage.PASSWORD_MATCH_CONSTRAIN_MESSAGE
        )
      );
  }

  @Test
  @Tag("differentPasswords")
  public void differentPasswordsTest() {
    userTemplate.setPassword(faker.bothify("####????"));
    userTemplate.setConfirmPassword(faker.bothify("????####"));
    Set<ConstraintViolation<UserRegisterDTO>> constraintViolations = validator.validate(
      userTemplate
    );

    assertThat(constraintViolations)
      .hasSize(1)
      .extracting(c -> c.getPropertyPath().toString(), c -> c.getMessage())
      .contains(
        tuple(
          "confirmPassword",
          ConstrainMessage.PASSWORD_MATCH_CONSTRAIN_MESSAGE
        )
      );
  }

  @Test
  @Tag("passwordsWithOnlyDigit")
  public void passwordsWithOnlyDigitTest() {
    userTemplate.setPassword(faker.bothify("########"));
    userTemplate.setConfirmPassword(userTemplate.getPassword());

    Set<ConstraintViolation<UserRegisterDTO>> constraintViolations = validator.validate(
      userTemplate
    );
    assertThat(constraintViolations)
      .hasSize(2)
      .extracting(c -> c.getPropertyPath().toString(), c -> c.getMessage())
      .contains(
        tuple("password", ConstrainMessage.PASSWORD_CONSTRAIN_MESSAGE),
        tuple("confirmPassword", ConstrainMessage.PASSWORD_CONSTRAIN_MESSAGE)
      );
  }

  @Test
  @Tag("passwordsWithAlphabetDigit")
  public void passwordsWithOnlyAlphabetTest() {
    userTemplate.setPassword(faker.bothify("????????"));
    userTemplate.setConfirmPassword(userTemplate.getPassword());
    Set<ConstraintViolation<UserRegisterDTO>> constraintViolations = validator.validate(
      userTemplate
    );
    assertThat(constraintViolations)
      .hasSize(2)
      .extracting(c -> c.getPropertyPath().toString(), c -> c.getMessage())
      .contains(
        tuple("password", ConstrainMessage.PASSWORD_CONSTRAIN_MESSAGE),
        tuple("confirmPassword", ConstrainMessage.PASSWORD_CONSTRAIN_MESSAGE)
      );
  }

  @Test
  @Tag("invalidZipCode")
  public void invalidZipCodeTest() {
    userTemplate.setZipCode(1013033);

    Set<ConstraintViolation<UserRegisterDTO>> constraintViolations = validator.validate(
      userTemplate
    );
    assertThat(constraintViolations)
      .hasSize(1)
      .extracting(c -> c.getPropertyPath().toString(), c -> c.getMessage())
      .contains(
        tuple("zipCode", ConstrainMessage.INVALID_FORMAT_CONSTRAIN_MESSAGE)
      );
  }

  @Test
  @Tag("emptyCity")
  public void emptyCityTest() {
    userTemplate.setCity("");
    Set<ConstraintViolation<UserRegisterDTO>> constraintViolations = validator.validate(
      userTemplate
    );
    assertThat(constraintViolations)
      .hasSize(1)
      .extracting(c -> c.getPropertyPath().toString(), c -> c.getMessage())
      .contains(tuple("city", ConstrainMessage.EMPTY_CONSTRAIN_MESSAGE));
  }

  @Test
  @Tag("emptyDivision")
  public void emptyDivisionyTest() {
    userTemplate.setDivision("");
    Set<ConstraintViolation<UserRegisterDTO>> constraintViolations = validator.validate(
      userTemplate
    );
    assertThat(constraintViolations)
      .hasSize(1)
      .extracting(c -> c.getPropertyPath().toString(), c -> c.getMessage())
      .contains(tuple("division", ConstrainMessage.EMPTY_CONSTRAIN_MESSAGE));
  }

  @Test
  @Tag("emptyAddress")
  public void emptyAddressTest() {
    userTemplate.setAddress("");
    Set<ConstraintViolation<UserRegisterDTO>> constraintViolations = validator.validate(
      userTemplate
    );
    assertThat(constraintViolations)
      .hasSize(1)
      .extracting(c -> c.getPropertyPath().toString(), c -> c.getMessage())
      .contains(tuple("address", ConstrainMessage.EMPTY_CONSTRAIN_MESSAGE));
  }

  @Test
  @Tag("emptyEducation")
  public void emptyEducationTest() {
    userTemplate.setEducation("");
    Set<ConstraintViolation<UserRegisterDTO>> constraintViolations = validator.validate(
      userTemplate
    );
    assertThat(constraintViolations)
      .hasSize(1)
      .extracting(c -> c.getPropertyPath().toString(), c -> c.getMessage())
      .contains(tuple("education", ConstrainMessage.EMPTY_CONSTRAIN_MESSAGE));
  }

  @Test
  @Tag("emptyGender")
  public void emptyGenderTest() {
    userTemplate.setGender("");
    Set<ConstraintViolation<UserRegisterDTO>> constraintViolations = validator.validate(
      userTemplate
    );
    assertThat(constraintViolations)
      .hasSize(1)
      .extracting(c -> c.getPropertyPath().toString(), c -> c.getMessage())
      .contains(tuple("gender", ConstrainMessage.EMPTY_CONSTRAIN_MESSAGE));
  }

  @Test
  @Tag("UnacceptTerm")
  public void UnacceptTermTest() {
    userTemplate.setAcceptTerm(false);
    Set<ConstraintViolation<UserRegisterDTO>> constraintViolations = validator.validate(
      userTemplate
    );
    assertThat(constraintViolations)
      .hasSize(1)
      .extracting(c -> c.getPropertyPath().toString(), c -> c.getMessage())
      .contains(tuple("acceptTerm", ConstrainMessage.TERM_CONSTRAIN_MESSAGE));
  }
}

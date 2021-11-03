package com.blissstock.mappingSite.dto;

import java.util.Date;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.blissstock.mappingSite.validation.constrains.PasswordData;
import com.blissstock.mappingSite.validation.constrains.PasswordMatch;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@EqualsAndHashCode(callSuper = true)
@PasswordMatch
// @PasswordMatch only work with PasswordData classees
public class UserRegisterDTO extends PasswordData{

  @Email
  private String email;

  @NotBlank(message = "This field is required")
  private String name;

  @Size(min = 8, message = "Password must be at least 8 characters")
  @Pattern(
    //check if the string contain at least one character and one digit
    regexp = "(?:[0-9]+[a-z]|[a-z]+[0-9])[a-z0-9]*$",
    flags = { Pattern.Flag.CASE_INSENSITIVE },
    message = "Password must contain at least one character and one digit"
  )
  private String password;

  @Size(min = 8, message = "Confirm Password must be at least 8 characters")
  @Pattern(
    //check if the string contain at least one character and one digit
    regexp = "(?:[0-9]+[a-z]|[a-z]+[0-9])[a-z0-9]*$",
    flags = { Pattern.Flag.CASE_INSENSITIVE },
    message = "Password must contain at least one character and one digit"
  )
  private String confirmPassword;

  @NotBlank(message = "This field is required")
  private String gender = "male";

  @NotBlank(message = "This field is required")
  private String phone;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  @PastOrPresent
  private Date dob = new Date();

  @Min(value = 10000, message = "invalid format")
  @Max(value = 99999, message = "invalid format")
  private int zipCode;

  @NotBlank(message = "This field is required")
  private String city;

  @NotBlank(message = "This field is required")
  private String division;

  @NotBlank(message = "This field is required")
  private String address;

  @NotBlank(message = "This field is required")
  private String education;

  @AssertTrue(message = "You must agree to Term & Condition")
  private boolean acceptTerm;
}

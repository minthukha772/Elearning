package com.blissstock.mappingSite.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class TeacherRegisterDTO {

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
  private String gender;

  @NotBlank(message = "This field is required")
  private String phone;

  @NotBlank(message = "This field is required")
  private String dob;

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

  @NotBlank(message = "This field is required")
  private String nrc;

  @NotBlank(message = "This field is required")
  private String award;

  @NotBlank(message = "This field is required")
  private String selfDescription;

  private String kPay;
  private String cbPay;
  private String wave;
  private String kbz;
  private String cb;
  private String aya;

  private boolean isAcceptTerm;
}

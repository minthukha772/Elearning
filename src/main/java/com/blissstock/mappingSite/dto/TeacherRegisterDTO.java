package com.blissstock.mappingSite.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.blissstock.mappingSite.constrains.PaymentMethod;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Data
public class TeacherRegisterDTO extends UserRegisterDTO{
  
  @NotBlank(message = "This field is required")
  private String nrc;

  @NotBlank(message = "This field is required")
  private String award;

  @NotBlank(message = "This field is required")
  private String selfDescription;

  @NotBlank(message = "This field is required")
  private String primaryServiceName;

  @NotBlank(message = "This field is required")
  private String primaryAccountName;

  @NotBlank(message = "This field is required")
  @Pattern(regexp = "[0-9]+", message = "Account Number cannot contain character")
  private String primaryAccountNumber;

  @NotBlank(message = "This field is required")
  private String secondaryServiceName;
  
  @NotBlank(message = "This field is required")
  private String secondaryAccountName;
  
  @NotBlank(message = "This field is required")
  @Pattern(regexp = "/[0-9]+", message = "Account Number cannot contain character")
  @NotBlank(message = "This field is required")
  private String secondaryAccountNumber;



}

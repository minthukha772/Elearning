package com.blissstock.mappingSite.dto;

import javax.validation.constraints.NotBlank;
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

  private String kPay;
  private String cbPay;
  private String wave;
  private String kbz;
  private String cb;
  private String aya;

  private boolean isAcceptTerm;
}

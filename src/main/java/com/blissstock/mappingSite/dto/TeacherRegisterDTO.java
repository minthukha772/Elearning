package com.blissstock.mappingSite.dto;

import java.util.ArrayList;
import java.util.List;

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

  private List<PaymentInfoDTO> paymentInfo;

  private boolean isAcceptTerm;

  public TeacherRegisterDTO(){
    paymentInfo = new ArrayList<>();
    for(PaymentMethod.)
  }
}

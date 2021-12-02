package com.blissstock.mappingSite.dto;

import com.blissstock.mappingSite.enums.PasswordResetType;
import com.blissstock.mappingSite.validation.ConstrainMessage;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Getter
@Setter
public class PasswordDTO {

  @NotBlank
  private String oldPassword;

  private String type;

  @Size(
    min = 8,
    max = 32,
    message = ConstrainMessage.PASSWORD_LENGTH_CONSTRAIN_MESSAGE
  )
  private String password;

  @Pattern(
    regexp = "^(?=.*?[A-Za-z])(?=.*?[0-9]).{8,}$",
    message = ConstrainMessage.PASSWORD_CONSTRAIN_MESSAGE
  )
  @Size(
    min = 8,
    max = 32,
    message = ConstrainMessage.PASSWORD_LENGTH_CONSTRAIN_MESSAGE
  )
  private String confirmPassword;
}

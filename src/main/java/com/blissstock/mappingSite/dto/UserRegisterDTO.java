package com.blissstock.mappingSite.dto;

import java.util.Date;
import java.util.LinkedHashMap;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.blissstock.mappingSite.interfaces.Confirmable;
import com.blissstock.mappingSite.utils.DateFormatter;
import com.blissstock.mappingSite.validation.ConstrainMessage;
import com.blissstock.mappingSite.validation.constrains.PasswordData;
import com.blissstock.mappingSite.validation.constrains.PasswordMatch;
import com.blissstock.mappingSite.validation.constrains.ValidEmail;

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
public class UserRegisterDTO extends PasswordData implements Confirmable {

  

  @ValidEmail
  private String email;

  @NotBlank(message = ConstrainMessage.EMPTY_CONSTRAIN_MESSAGE)
  private String name;

  @Pattern(
    regexp = "^(?=.*?[A-Za-z])(?=.*?[0-9]).{8,}$",
    message = ConstrainMessage.PASSWORD_CONSTRAIN_MESSAGE
  )
  @Size(min = 8, max = 32, message = ConstrainMessage.PASSWORD_LENGTH_CONSTRAIN_MESSAGE)
  private String password;

  @Pattern(
    regexp = "^(?=.*?[A-Za-z])(?=.*?[0-9]).{8,}$",
    message = ConstrainMessage.PASSWORD_CONSTRAIN_MESSAGE
  )
  @Size(min = 8, max = 32, message = ConstrainMessage.PASSWORD_LENGTH_CONSTRAIN_MESSAGE)
  private String confirmPassword;

  @NotBlank(message = ConstrainMessage.EMPTY_CONSTRAIN_MESSAGE)
  private String gender = "male";

  @NotBlank(message = ConstrainMessage.EMPTY_CONSTRAIN_MESSAGE)
  private String phone;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  @Past  
  private Date dob = new Date();

  @Max(value = 99999, message = ConstrainMessage.INVALID_FORMAT_CONSTRAIN_MESSAGE)
  private int zipCode;

  @NotBlank(message = ConstrainMessage.EMPTY_CONSTRAIN_MESSAGE)
  private String city;

  @NotBlank(message = ConstrainMessage.EMPTY_CONSTRAIN_MESSAGE)
  private String division;

  @NotBlank(message = ConstrainMessage.EMPTY_CONSTRAIN_MESSAGE)
  private String address;

  @NotBlank(message = ConstrainMessage.EMPTY_CONSTRAIN_MESSAGE)
  private String education;

  @AssertTrue(message = ConstrainMessage.TERM_CONSTRAIN_MESSAGE)
  private boolean acceptTerm;

  @Override
  public LinkedHashMap<String, String> toMap() {
    LinkedHashMap<String, String> map = new LinkedHashMap<>();
    map.put("Email", this.email);
    map.put("Name", this.name);
    map.put("Phone Number", this.phone);
    map.put("Gender", this.gender);
    map.put("Date of Birth", DateFormatter.format(this.dob));
    map.put("Zip Code", this.zipCode + "");
    map.put("City", this.city);
    map.put("Division", this.division);
    map.put("Address", this.address);
    map.put("Education", this.education);
    return map;
  }
}

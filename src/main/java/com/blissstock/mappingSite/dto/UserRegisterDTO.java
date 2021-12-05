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

// import org.hibernate.boot.model.relational.Database;
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


  public UserRegisterDTO() {
  }


  public UserRegisterDTO(String email, String name, String password, String confirmPassword, String gender, String phone, Date dob, int zipCode, String city, String division, String address, String education, boolean acceptTerm) {
    this.email = email;
    this.name = name;
    this.password = password;
    this.confirmPassword = confirmPassword;
    this.gender = gender;
    this.phone = phone;
    this.dob = dob;
    this.zipCode = zipCode;
    this.city = city;
    this.division = division;
    this.address = address;
    this.education = education;
    this.acceptTerm = acceptTerm;
  }


  public String getEmail() {
    return this.email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPassword() {
    return this.password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getConfirmPassword() {
    return this.confirmPassword;
  }

  public void setConfirmPassword(String confirmPassword) {
    this.confirmPassword = confirmPassword;
  }

  public String getGender() {
    return this.gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public String getPhone() {
    return this.phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public Date getDob() {
    return this.dob;
  }

  public void setDob(Date dob) {
    this.dob = dob;
  }

  public int getZipCode() {
    return this.zipCode;
  }

  public void setZipCode(int zipCode) {
    this.zipCode = zipCode;
  }

  public String getCity() {
    return this.city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getDivision() {
    return this.division;
  }

  public void setDivision(String division) {
    this.division = division;
  }

  public String getAddress() {
    return this.address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getEducation() {
    return this.education;
  }

  public void setEducation(String education) {
    this.education = education;
  }

  public boolean isAcceptTerm() {
    return this.acceptTerm;
  }

  public boolean getAcceptTerm() {
    return this.acceptTerm;
  }

  public void setAcceptTerm(boolean acceptTerm) {
    this.acceptTerm = acceptTerm;
  }


}

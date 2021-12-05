package com.blissstock.mappingSite.entity;

import com.blissstock.mappingSite.dto.TeacherRegisterDTO;
import com.blissstock.mappingSite.dto.UserRegisterDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Data
@Table(name = "user_account")
public class UserAccount {

  @Id
  @Column(name = "mail", length = 255)
  @NotBlank(message = "Please enter email address")
  private String mail;

  @Column(name = "isMailVerified", nullable = false)
  private boolean isMailVerified = false;

  @NotBlank(message = "Please enter password.")
  @Column(name = "password", length = 64)
  private String password;

  @Column(name = "role", length = 15)
  private String role;

  @Column(name = "account_status", length = 10)
  private String accountStatus;

  @NotNull
  @Past
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  @Column(name = "registered_date")
  private Date registeredDate;

  //mapping
  @OneToOne(
    fetch = FetchType.LAZY,
    cascade = CascadeType.ALL,
    mappedBy = "userAccount"
  )
  @JsonIgnore
  private UserInfo userInfo;

  public static UserAccount fromRegisterDTO(UserRegisterDTO userRegisterDTO) {
    UserAccount userAccount = new UserAccount();
    userAccount.mail = userRegisterDTO.getEmail();
    userAccount.password = userRegisterDTO.getPassword();
    userAccount.role =
      userRegisterDTO instanceof TeacherRegisterDTO ? "teacher" : "student";
    userAccount.registeredDate = GregorianCalendar.getInstance().getTime();
    return userAccount;
  }

  //Constructors

  public UserAccount() {
  }

  public UserAccount(String mail, boolean isMailVerified, String password, String role, String accountStatus, Date registeredDate, UserInfo userInfo) {
    this.mail = mail;
    this.isMailVerified = isMailVerified;
    this.password = password;
    this.role = role;
    this.accountStatus = accountStatus;
    this.registeredDate = registeredDate;
    this.userInfo = userInfo;
  }

  //getters and setters

  public String getMail() {
    return this.mail;
  }

  public void setMail(String mail) {
    this.mail = mail;
  }

  public boolean isIsMailVerified() {
    return this.isMailVerified;
  }

  public boolean getIsMailVerified() {
    return this.isMailVerified;
  }

  public void setIsMailVerified(boolean isMailVerified) {
    this.isMailVerified = isMailVerified;
  }

  public String getPassword() {
    return this.password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getRole() {
    return this.role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public String getAccountStatus() {
    return this.accountStatus;
  }

  public void setAccountStatus(String accountStatus) {
    this.accountStatus = accountStatus;
  }

  public Date getRegisteredDate() {
    return this.registeredDate;
  }

  public void setRegisteredDate(Date registeredDate) {
    this.registeredDate = registeredDate;
  }

  public UserInfo getUserInfo() {
    return this.userInfo;
  }

  public void setUserInfo(UserInfo userInfo) {
    this.userInfo = userInfo;
  }



}

package com.blissstock.mappingSite.entity;

import com.blissstock.mappingSite.dto.TeacherRegisterDTO;
import com.blissstock.mappingSite.dto.UserRegisterDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;
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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user_account")
public class UserAccount {

  @Column(name = "account_id")
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long accountId;

  @Column(name = "mail", length = 255)
  @NotBlank(message = "Please enter email address")
  private String mail;

  @Column(name = "isMailVerified", nullable = false)
  private boolean isMailVerified = false;

  @Column(name = "photo")
  private String photo;

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
	userAccount.role = userRegisterDTO instanceof TeacherRegisterDTO?"teacher":"student";
    return userAccount;
  }
}

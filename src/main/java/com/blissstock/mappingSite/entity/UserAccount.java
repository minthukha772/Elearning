package com.blissstock.mappingSite.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Data
@Table(name = "user_account")
public class UserAccount {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

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

/*   //mapping
  @OneToOne(
    fetch = FetchType.LAZY,
    cascade = CascadeType.ALL,
    mappedBy = "userAccount"
  )
  @JsonIgnore
  
  private UserInfo userInfo; */


}

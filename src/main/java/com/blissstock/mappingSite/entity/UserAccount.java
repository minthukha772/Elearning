package com.blissstock.mappingSite.entity;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
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
  
	@Column(name = "role", length = 25)
	private String role;
  
	@Column(name = "account_status", length = 10)
	private String accountStatus;
  
	@NotNull
	@Past
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "registered_date")
	private Date registeredDate;


	public UserAccount(Long id, String mail, boolean isMailVerified, String password, String role, String accountStatus, Date registeredDate) {
		this.id = id;
		this.mail = mail;
		this.isMailVerified = isMailVerified;
		this.password = password;
		this.role = role;
		this.accountStatus = accountStatus;
		this.registeredDate = registeredDate;
	}
	
}


package com.blissstock.mappingSite.entity;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
@Table(name = "user_info")
public class UserInfo {
	
	@Column(name = "uid")
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long uid;
	
	@Column(name = "user_name", length = 100)
	@NotBlank(message="Please enter your name")
	private String userName;
	
	@NotBlank(message="Please enter phone number.")
    @Size(max = 20, min = 8, message = "Phone number should be under 11 digits")
	@Column(name="phone_no")
	private String phoneNo;
	
	@NotBlank(message="Please choose gender.")
	@Column(name="gender", length = 20)
	private String gender;

	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name="birth_date")
	private Date birthDate;

	@NotBlank(message="Please enter postal code.")
	@Column(name="postal_code")
	private String postalCode;

	@NotBlank(message="Please enter city.")
	@Column(name="city", length = 50)
	private String city;
	
	@NotBlank(message="Please enter division")
	@Column(name="division", length = 50)
	private String division;

	@NotBlank(message="Please enter address.")
	@Column(name="address", length = 255)
	private String address;
	
	@NotBlank(message="Please fill education level.")
	@Column(name="education", length = 255)
	private String education;

	@Column(name="nrc", length = 30)
	private String nrc;

	@Column(name="certificate")
	private String certificate;

	@Column(name="self_description")
	private String selfDescription;
	
	//mapping
	@OneToOne(fetch=FetchType.EAGER,cascade=CascadeType.ALL)
	UserAccount userAccount;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy="userInfo")
	@JsonIgnore
	private List<Certificate> certificateInfo= new ArrayList<>();

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy="userInfo")
	@JsonIgnore
	private List<PaymentAccount> paymentAccount= new ArrayList<>();
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy="userInfo")
	@JsonIgnore
	private List<LeaveInfo> leaveInfo= new ArrayList<>();

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy="userInfo")
	@JsonIgnore
	private List<Review> review= new ArrayList<>();

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy="userInfo")
	@JsonIgnore
	private List<PriorityCourse> priorityCourse= new ArrayList<>();

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy="userInfo")
	@JsonIgnore
	private List<PaymentReceive> paymentReceive= new ArrayList<>();

	@ManyToMany(fetch=FetchType.LAZY, cascade = {CascadeType.MERGE},mappedBy = "userInfo")
	@JsonIgnore
	private List<CourseInfo> courseInfo = new ArrayList<>();
}


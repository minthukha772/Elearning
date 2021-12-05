  package com.blissstock.mappingSite.entity;

import com.blissstock.mappingSite.dto.TeacherRegisterDTO;
import com.blissstock.mappingSite.dto.UserRegisterDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user_info")
public class UserInfo {

  @Column(name = "uid")
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long uid;

  @Column(name = "photo")
  private String photo;

  @Column(name = "user_name", length = 100)
  @NotBlank(message = "Please enter your name")
  private String userName;

  @NotBlank(message = "Please enter phone number.")
  @Size(max = 20, min = 8, message = "Phone number should be under 11 digits")
  @Column(name = "phone_no")
  private String phoneNo;

  @NotBlank(message = "Please choose gender.")
  @Column(name = "gender", length = 20)
  private String gender;

  @NotNull
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  @Column(name = "birth_date")
  private Date birthDate;

  @NotBlank(message = "Please enter postal code.")
  @Column(name = "postal_code")
  private String postalCode;

  @NotBlank(message = "Please enter city.")
  @Column(name = "city", length = 50)
  private String city;

  @NotBlank(message = "Please enter division")
  @Column(name = "division", length = 50)
  private String division;

  @NotBlank(message = "Please enter address.")
  @Column(name = "address", length = 255)
  private String address;

  @NotBlank(message = "Please fill education level.")
  @Column(name = "education", length = 255)
  private String education;

  @Column(name = "nrc", length = 30)
  private String nrc;

  @Column(name = "certificate")
  private String certificate;

  @Column(name = "self_description")
  private String selfDescription;

  //mapping
  @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  UserAccount userAccount;

  @OneToMany(
    fetch = FetchType.LAZY,
    cascade = CascadeType.ALL,
    mappedBy = "userInfo"
  )
  @JsonIgnore
  private List<Certificate> certificateInfo = new ArrayList<>();

  @OneToMany(
    fetch = FetchType.LAZY,
    cascade = CascadeType.ALL,
    mappedBy = "userInfo"
  )
  @JsonIgnore
  private List<PaymentAccount> paymentAccount = new ArrayList<>();

  @OneToMany(
    fetch = FetchType.LAZY,
    cascade = CascadeType.ALL,
    mappedBy = "userInfo"
  )
  @JsonIgnore
  private List<LeaveInfo> leaveInfo = new ArrayList<>();

  @OneToMany(
    fetch = FetchType.LAZY,
    cascade = CascadeType.ALL,
    mappedBy = "userInfo"
  )
  @JsonIgnore
  private List<Review> review = new ArrayList<>();

  @OneToMany(
    fetch = FetchType.LAZY,
    cascade = CascadeType.ALL,
    mappedBy = "userInfo"
  )
  @JsonIgnore
  private List<PriorityCourse> priorityCourse = new ArrayList<>();

  @OneToMany(
    fetch = FetchType.LAZY,
    cascade = CascadeType.ALL,
    mappedBy = "userInfo"
  )
  @JsonIgnore
  private List<PaymentReceive> paymentReceive = new ArrayList<>();

  @ManyToMany(
    fetch = FetchType.LAZY,
    cascade = { CascadeType.MERGE },
    mappedBy = "userInfo"
  )
  @JsonIgnore
  private List<CourseInfo> courseInfo = new ArrayList<>();

  public static UserInfo fromRegisterDTO(UserRegisterDTO userRegisterDTO) {
    UserInfo userInfo = new UserInfo();
    userInfo.userName = userRegisterDTO.getName();
    userInfo.phoneNo = userRegisterDTO.getPhone();
    userInfo.gender = userRegisterDTO.getGender();
    userInfo.birthDate = userRegisterDTO.getDob();
    userInfo.postalCode = userRegisterDTO.getZipCode() + "";
    userInfo.city = userRegisterDTO.getCity();
    userInfo.division = userRegisterDTO.getDivision();
    userInfo.address = userRegisterDTO.getAddress();
    userInfo.education = userRegisterDTO.getName();

    if (userRegisterDTO instanceof TeacherRegisterDTO) {
      TeacherRegisterDTO teacherRegisterDTO = (TeacherRegisterDTO) userRegisterDTO;
      userInfo.nrc = teacherRegisterDTO.getNrc();
      userInfo.selfDescription = teacherRegisterDTO.getSelfDescription();
      userInfo.certificate = teacherRegisterDTO.getAward();
    }

    return userInfo;
  }

  // @ManyToMany(fetch = FetchType.LAZY)
	// 	@JoinTable(
	// 			name = "join_user_course", 
	// 			joinColumns = {@JoinColumn(name = "uid")} ,
	// 			inverseJoinColumns = {@JoinColumn(name = "course_id")}
	// 			) 
	// 	private List<CourseInfo> courseInfo = new ArrayList<>();

  //Constructors


  public UserInfo() {
  }

  public UserInfo(Long uid, String photo, String userName, String phoneNo, String gender, Date birthDate, String postalCode, String city, String division, String address, String education, String nrc, String certificate, String selfDescription, UserAccount userAccount, List<Certificate> certificateInfo, List<PaymentAccount> paymentAccount, List<LeaveInfo> leaveInfo, List<Review> review, List<PriorityCourse> priorityCourse, List<PaymentReceive> paymentReceive, List<CourseInfo> courseInfo) {
    this.uid = uid;
    this.photo = photo;
    this.userName = userName;
    this.phoneNo = phoneNo;
    this.gender = gender;
    this.birthDate = birthDate;
    this.postalCode = postalCode;
    this.city = city;
    this.division = division;
    this.address = address;
    this.education = education;
    this.nrc = nrc;
    this.certificate = certificate;
    this.selfDescription = selfDescription;
    this.userAccount = userAccount;
    this.certificateInfo = certificateInfo;
    this.paymentAccount = paymentAccount;
    this.leaveInfo = leaveInfo;
    this.review = review;
    this.priorityCourse = priorityCourse;
    this.paymentReceive = paymentReceive;
    this.courseInfo = courseInfo;
  }
    

  public Long getUid() {
    return this.uid;
  }

  public void setUid(Long uid) {
    this.uid = uid;
  }

  public String getPhoto() {
    return this.photo;
  }

  public void setPhoto(String photo) {
    this.photo = photo;
  }

  public String getUserName() {
    return this.userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getPhoneNo() {
    return this.phoneNo;
  }

  public void setPhoneNo(String phoneNo) {
    this.phoneNo = phoneNo;
  }

  public String getGender() {
    return this.gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public Date getBirthDate() {
    return this.birthDate;
  }

  public void setBirthDate(Date birthDate) {
    this.birthDate = birthDate;
  }

  public String getPostalCode() {
    return this.postalCode;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
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

  public String getNrc() {
    return this.nrc;
  }

  public void setNrc(String nrc) {
    this.nrc = nrc;
  }

  public String getCertificate() {
    return this.certificate;
  }

  public void setCertificate(String certificate) {
    this.certificate = certificate;
  }

  public String getSelfDescription() {
    return this.selfDescription;
  }

  public void setSelfDescription(String selfDescription) {
    this.selfDescription = selfDescription;
  }

  public UserAccount getUserAccount() {
    return this.userAccount;
  }

  public void setUserAccount(UserAccount userAccount) {
    this.userAccount = userAccount;
  }

  public List<Certificate> getCertificateInfo() {
    return this.certificateInfo;
  }

  public void setCertificateInfo(List<Certificate> certificateInfo) {
    this.certificateInfo = certificateInfo;
  }

  public List<PaymentAccount> getPaymentAccount() {
    return this.paymentAccount;
  }

  public void setPaymentAccount(List<PaymentAccount> paymentAccount) {
    this.paymentAccount = paymentAccount;
  }

  public List<LeaveInfo> getLeaveInfo() {
    return this.leaveInfo;
  }

  public void setLeaveInfo(List<LeaveInfo> leaveInfo) {
    this.leaveInfo = leaveInfo;
  }

  public List<Review> getReview() {
    return this.review;
  }

  public void setReview(List<Review> review) {
    this.review = review;
  }

  public List<PriorityCourse> getPriorityCourse() {
    return this.priorityCourse;
  }

  public void setPriorityCourse(List<PriorityCourse> priorityCourse) {
    this.priorityCourse = priorityCourse;
  }

  public List<PaymentReceive> getPaymentReceive() {
    return this.paymentReceive;
  }

  public void setPaymentReceive(List<PaymentReceive> paymentReceive) {
    this.paymentReceive = paymentReceive;
  }

  public List<CourseInfo> getCourseInfo() {
    return this.courseInfo;
  }

  public void setCourseInfo(List<CourseInfo> courseInfo) {
    this.courseInfo = courseInfo;
  }
  
}

package com.blissstock.mappingSite.entity;

import com.blissstock.mappingSite.dto.TeacherRegisterDTO;
import com.blissstock.mappingSite.dto.UserRegisterDTO;
import com.blissstock.mappingSite.interfaces.Profile;
import com.blissstock.mappingSite.utils.DateFormatter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.servlet.FlashMapManager;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user_info")
public class UserInfo implements Profile{

  @Column(name = "uid")
  @Id
  private Long uid;

  @Column(name = "photo")
  private String photo;

  @Column(name = "user_name", length = 100)
  private String userName;

  @Size(max = 15, min = 6, message = "Invalid Phone Number")
  @Column(name = "phone_no")
  private String phoneNo;

  @Column(name = "gender", length = 20)
  private String gender;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  @Column(name = "birth_date")
  private Date birthDate;

  @Column(name = "postal_code")
  private String postalCode;

  @Column(name = "city", length = 50)
  private String city;

  @Column(name = "division", length = 50)
  private String division;

  @Column(name = "address", length = 255)
  private String address;

  @Column(name = "education", length = 255)
  private String education;

  @Column(name = "nrc", length = 30)
  private String nrc;

  @Column(name = "self_description")
  private String selfDescription;

  //mapping
  @OneToOne(fetch = FetchType.EAGER)
  @MapsId
  @JoinColumn(name = "id")
  UserAccount userAccount;

/*   @OneToMany(
    fetch = FetchType.LAZY,
    cascade = CascadeType.ALL,
    mappedBy = "userInfo"
  )
  @JsonIgnore
  private List<Certificate> certificateInfo = new ArrayList<>(); */

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
    mappedBy="userInfo"
    )
	@JsonIgnore
	private List<JoinCourseUser> join= new ArrayList<>();

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
    }

    return userInfo;
  }
  @Override
  public LinkedHashMap<String, String> toMapStudent() {
    LinkedHashMap<String, String> map = new LinkedHashMap<>();
    //map.put("Email", this.email);
    map.put("Name", this.userName);
    map.put("Phone Number", this.phoneNo);
    map.put("Gender", this.gender);
    map.put("Date of Birth", DateFormatter.format(this.birthDate));
    map.put("Zip Code", this.postalCode + "");
    map.put("City", this.city);
    map.put("Division", this.division);
    map.put("Address", this.address);
    map.put("Education", this.education);
    return map;
  }

  @Override
  public LinkedHashMap<String, String> toMapTeacher() {
    LinkedHashMap<String, String> map = new LinkedHashMap<>();
    //map.put("Email", this.email);
    map.put("Name", this.userName);
    map.put("Gender", this.gender);
    map.put("Date of Birth", DateFormatter.format(this.birthDate));
    map.put("Education", this.education);
    map.put("SelfDescription", this.selfDescription);
    return map;
  }

  //Constructors

  public UserInfo() {
  }

  public UserInfo(Long uid, String photo, String userName, String phoneNo, String gender, Date birthDate, String postalCode, String city, String division, String address, String education, String nrc, String selfDescription, UserAccount userAccount, List<PaymentAccount> paymentAccount, List<JoinCourseUser> join) {
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
    this.selfDescription = selfDescription;
    this.userAccount = userAccount;
    this.paymentAccount = paymentAccount;
    this.join = join;
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

  public List<PaymentAccount> getPaymentAccount() {
    return this.paymentAccount;
  }

  public void setPaymentAccount(List<PaymentAccount> paymentAccount) {
    this.paymentAccount = paymentAccount;
  }

  public List<JoinCourseUser> getJoin() {
    return this.join;
  }

  public void setJoin(List<JoinCourseUser> join) {
    this.join = join;
  }

 
}

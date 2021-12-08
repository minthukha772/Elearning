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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "user_info")
public class UserInfo implements Profile {

  @Column(name = "uid")
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long uid;

  @Column(name="photo")
	private String photo;
  
  @Column(name = "user_name", length = 100)
  //@NotBlank(message = "Please enter your name")
  protected String userName;

  //@NotBlank(message = "Please enter phone number.")
  //@Size(max = 20, min = 8, message = "Phone number should be under 11 digits")
  @Column(name = "phone_no")
  protected String phoneNo;

  //@NotBlank(message = "Please choose gender.")
  @Column(name = "gender", length = 20)
  protected String gender;

  //@NotNull
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  @Column(name = "birth_date")
  protected Date birthDate;

  //@NotBlank(message = "Please enter postal code.")
  @Column(name = "postal_code")
  protected String postalCode;

  //@NotBlank(message = "Please enter city.")
  @Column(name = "city", length = 50)
  protected String city;

  //@NotBlank(message = "Please enter division")
  @Column(name = "division", length = 50)
  protected String division;

  //@NotBlank(message = "Please enter address.")
  @Column(name = "address", length = 255)
  protected String address;

  //@NotBlank(message = "Please fill education level.")
  @Column(name = "education", length = 255)
  protected String education;

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
    fetch = FetchType.EAGER,
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
    map.put("Certificate", this.certificate);
    map.put("SelfDescription", this.selfDescription);
    return map;
  }

 
}

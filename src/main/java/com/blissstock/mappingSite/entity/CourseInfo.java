package com.blissstock.mappingSite.entity;

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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Getter
@Setter
@Table(name = "course_info")
public class CourseInfo {

  @Column(name = "course_id")
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long courseId;

  @Column(name = "course_name", length = 100)
  //@NotBlank(message="Please enter course name")
  private String courseName;

  @Column(name = "class_type", length = 20)
  ///@NotBlank(message="Please enter class type")
  private String classType;

  @Column(name = "category", length = 100)
  //@NotBlank(message="Please choose category")
  private String category;

  @Column(name = "level", length = 15)
  //@NotBlank(message="Please choose course level")
  private String level;

  @Column(name = "about_course", length = 250)
  //@NotBlank(message="Please enter about course")
  private String aboutCourse;

  @Column(name = "student_num", length = 20)
  private int stuNum;

  //@NotNull
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  @Column(name = "start_date")
  private Date startDate;

  //@NotNull
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  @Column(name = "end_date")
  private Date endDate;

  //@NotBlank(message="Please enter course fees")
  @Column(name = "course_fees")
  private int fees;

  @Column(name = "isCourseApproved", nullable = false)
  private boolean isCourseApproved = false;

  //mapping
  @OneToMany(
    fetch = FetchType.LAZY,
    cascade = CascadeType.ALL,
    mappedBy = "courseInfo"
  )
  @JsonIgnore
  private List<CourseTime> courseTime = new ArrayList<>();

  @OneToMany(
    fetch = FetchType.LAZY,
    cascade = CascadeType.ALL,
    mappedBy = "courseInfo"
  )
  @JsonIgnore
  private List<Syllabus> syllabus = new ArrayList<>();

  @OneToMany(
    fetch = FetchType.LAZY,
    cascade = CascadeType.ALL,
    mappedBy = "courseInfo"
  )
  @JsonIgnore
  private List<Test> test = new ArrayList<>();

  @OneToMany(
    fetch = FetchType.LAZY,
    cascade = CascadeType.ALL,
    mappedBy = "courseInfo"
  )
  @JsonIgnore
  private List<JoinCourseUser> join = new ArrayList<>();

  //Constructors

  public CourseInfo() {
  }

  public CourseInfo(Long courseId, String courseName, String classType, String category, String level, String aboutCourse, int stuNum, Date startDate, Date endDate, int fees, boolean isCourseApproved, List<CourseTime> courseTime, List<Syllabus> syllabus, List<Test> test, List<JoinCourseUser> join) {
    this.courseId = courseId;
    this.courseName = courseName;
    this.classType = classType;
    this.category = category;
    this.level = level;
    this.aboutCourse = aboutCourse;
    this.stuNum = stuNum;
    this.startDate = startDate;
    this.endDate = endDate;
    this.fees = fees;
    this.isCourseApproved = isCourseApproved;
    this.courseTime = courseTime;
    this.syllabus = syllabus;
    this.test = test;
    this.join = join;
  }

  public Long getCourseId() {
    return this.courseId;
  }

  public void setCourseId(Long courseId) {
    this.courseId = courseId;
  }

  public String getCourseName() {
    return this.courseName;
  }

  public void setCourseName(String courseName) {
    this.courseName = courseName;
  }

  public String getClassType() {
    return this.classType;
  }

  public void setClassType(String classType) {
    this.classType = classType;
  }

  public String getCategory() {
    return this.category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getLevel() {
    return this.level;
  }

  public void setLevel(String level) {
    this.level = level;
  }

  public String getAboutCourse() {
    return this.aboutCourse;
  }

  public void setAboutCourse(String aboutCourse) {
    this.aboutCourse = aboutCourse;
  }

  public int getStuNum() {
    return this.stuNum;
  }

  public void setStuNum(int stuNum) {
    this.stuNum = stuNum;
  }

  public Date getStartDate() {
    return this.startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public Date getEndDate() {
    return this.endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public int getFees() {
    return this.fees;
  }

  public void setFees(int fees) {
    this.fees = fees;
  }

  public boolean isIsCourseApproved() {
    return this.isCourseApproved;
  }

  public boolean getIsCourseApproved() {
    return this.isCourseApproved;
  }

  public void setIsCourseApproved(boolean isCourseApproved) {
    this.isCourseApproved = isCourseApproved;
  }

  public List<CourseTime> getCourseTime() {
    return this.courseTime;
  }

  public void setCourseTime(List<CourseTime> courseTime) {
    this.courseTime = courseTime;
  }

  public List<Syllabus> getSyllabus() {
    return this.syllabus;
  }

  public void setSyllabus(List<Syllabus> syllabus) {
    this.syllabus = syllabus;
  }

  public List<Test> getTest() {
    return this.test;
  }

  public void setTest(List<Test> test) {
    this.test = test;
  }

  public List<JoinCourseUser> getJoin() {
    return this.join;
  }

  public void setJoin(List<JoinCourseUser> join) {
    this.join = join;
  }


}

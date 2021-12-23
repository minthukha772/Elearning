package com.blissstock.mappingSite.entity;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
//import javax.validation.constraints.Past;
//import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonIgnore;
@Getter
@Setter
@Entity
@Table(name = "testing_course")
public class CourseTesting {

    @Column(name = "course_id")
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long courseId;
	
	@Column(name = "course_name", length = 100)
	@NotBlank(message="Please enter course name")
	private String courseName;

    @Column(name = "class_type", length = 20)
	@NotBlank(message="Please enter class type")
	private String classType;

    @Column(name = "category", length = 100)
	@NotBlank(message="Please choose category")
	private String category;

    @Column(name = "level", length = 15)
	@NotBlank(message="Please choose course level")
	private String level;
	
	@Column(name = "about_course", length = 100)
	@NotBlank(message="Please enter course name")
	private String aboutCourse;

    @Column(name = "class_link", length = 20)
	@NotBlank(message="Please enter class type")
	private String classLink;

    @Column(name = "course_fees")
	@NotNull(message="Please enter fee")
	private int fees;

    @Column(name = "student_num", length = 20)
	@NotNull(message="Please enter student number")
	private int stuNum;

    @NotNull
	@DateTimeFormat(pattern = "MM-dd-yyyy")
    @Column(name="start_date")
	private Date startDate = new Date();
    
    @NotNull
	@DateTimeFormat(pattern = "MM-dd-yyyy")
    // @NotNull
	@Column(name="end_date")
    //@DateTimeFormat(pattern = "MM-dd-yyyy")
	private Date endDate = new Date();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy="courseInfo")
	@JsonIgnore
	private List<CourseTime> courseTime= new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
		@JoinTable(
				name = "join_course_user", 
				joinColumns = {@JoinColumn(name = "course_id")} ,
				inverseJoinColumns = {@JoinColumn(name = "uid")}
				) 

		private List<UserInfo> userInfo = new ArrayList<>();


    public CourseTesting(Long courseId, String courseName, String classType, String category, String level, String aboutCourse, String classLink, int fees, int stuNum, Date startDate, Date endDate, List<CourseTime> courseTime, List<UserInfo> userInfo) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.classType = classType;
        this.category = category;
        this.level = level;
        this.aboutCourse = aboutCourse;
        this.classLink = classLink;
        this.fees = fees;
        this.stuNum = stuNum;
        this.startDate = startDate;
        this.endDate = endDate;
        this.courseTime = courseTime;
        this.userInfo = userInfo;
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

    public List<CourseTime> getCourseTime() {
        return this.courseTime;
    }

    public void setCourseTime(List<CourseTime> courseTime) {
        this.courseTime = courseTime;
    }

    public List<UserInfo> getUserInfo() {
        return this.userInfo;
    }

    public void setUserInfo(List<UserInfo> userInfo) {
        this.userInfo = userInfo;
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

    public String getClassLink() {
        return this.classLink;
    }

    public void setClassLink(String classLink) {
        this.classLink = classLink;
    }

    public int getFees() {
        return this.fees;
    }

    public void setFees(int fees) {
        this.fees = fees;
    }

    public CourseTesting() {
    }
    
}

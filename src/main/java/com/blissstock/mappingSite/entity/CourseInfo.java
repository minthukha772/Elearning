package com.blissstock.mappingSite.entity;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "course_info")
public class CourseInfo {
	
	@Column(name = "course_id")
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long courseId;
	
	@Column(name = "course_name", length = 100)
	// @NotBlank(message="Please enter course name")
	private String courseName;

    @Column(name = "class_type", length = 20)
	// @NotBlank(message="Please enter class type")
	private String classType;

    @Column(name = "category", length = 100)
	// @NotBlank(message="Please choose category")
	private String category;

    @Column(name = "level", length = 15)
	// @NotBlank(message="Please choose course level")
	private String level;

    @Column(name = "about_course", length = 250)
	// @NotBlank(message="Please enter about course")
	private String aboutCourse;

	@Column(name = "course_title", length = 250)
	// @NotBlank(message="Please enter course title")
	private String title;

	@Column(name = "course_content", length = 500)
	// @NotBlank(message="Please enter course content")
	private String content;
	
    // @NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name="start_date")
	private Date startDate;

    // @NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name="end_date")
	private Date endDate;

    // @NotBlank(message="Please enter course fees")
	@Column(name="course_fees")
	private int fees;

    // @NotBlank(message="Please enter class link")
    @Column(name = "class_link")
	private String classLink;
	
    @Column(name= "isCourseApproved", nullable = false)
	private String isCourseApproved;

	//mapping
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy="courseInfo")
	@JsonIgnore
	private List<CourseTime> courseTime= new ArrayList<>();

	/*@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy="courseInfo")
	@JsonIgnore
	private List<Syllabus> syllabus= new ArrayList<>();*/

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy="courseInfo")
	@JsonIgnore
	private List<Test> test= new ArrayList<>();
	
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy="courseInfo")
	@JsonIgnore
	private List<LeaveInfo> leaveInfo= new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy="courseInfo")
	@JsonIgnore
	private List<Review> review= new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy="courseInfo")
	@JsonIgnore
	private List<PriorityCourse> priorityCourse= new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy="courseInfo")
	@JsonIgnore
	private List<PaymentReceive> paymentReceive= new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
		@JoinTable(
				name = "join_course_user", 
				joinColumns = {@JoinColumn(name = "course_id")} ,
				inverseJoinColumns = {@JoinColumn(name = "uid")}
				) 
		private List<UserInfo> userInfo = new ArrayList<>();


	public CourseInfo() {
	}

	public CourseInfo(Long courseId, String courseName, String classType, String category, String level, String aboutCourse, Date startDate, Date endDate, int fees, String classLink, String isCourseApproved, List<CourseTime> courseTime, List<Test> test, List<LeaveInfo> leaveInfo, List<Review> review, List<PriorityCourse> priorityCourse, List<PaymentReceive> paymentReceive, List<UserInfo> userInfo) {
		this.courseId = courseId;
		this.courseName = courseName;
		this.classType = classType;
		this.category = category;
		this.level = level;
		this.aboutCourse = aboutCourse;
		this.startDate = startDate;
		this.endDate = endDate;
		this.fees = fees;
		this.classLink = classLink;
		this.isCourseApproved = isCourseApproved;
		this.courseTime = courseTime;
		this.test = test;
		this.leaveInfo = leaveInfo;
		this.review = review;
		this.priorityCourse = priorityCourse;
		this.paymentReceive = paymentReceive;
		this.userInfo = userInfo;
	}


	public CourseInfo(Long courseId, String courseName, String classType, String category, String level, String aboutCourse, String title, String content, Date startDate, Date endDate, int fees, String classLink, String isCourseApproved, List<CourseTime> courseTime, List<Test> test, List<LeaveInfo> leaveInfo, List<Review> review, List<PriorityCourse> priorityCourse, List<PaymentReceive> paymentReceive, List<UserInfo> userInfo) {
		this.courseId = courseId;
		this.courseName = courseName;
		this.classType = classType;
		this.category = category;
		this.level = level;
		this.aboutCourse = aboutCourse;
		this.title = title;
		this.content = content;
		this.startDate = startDate;
		this.endDate = endDate;
		this.fees = fees;
		this.classLink = classLink;
		this.isCourseApproved = isCourseApproved;
		this.courseTime = courseTime;
		this.test = test;
		this.leaveInfo = leaveInfo;
		this.review = review;
		this.priorityCourse = priorityCourse;
		this.paymentReceive = paymentReceive;
		this.userInfo = userInfo;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
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

	public String getClassLink() {
		return this.classLink;
	}

	public void setClassLink(String classLink) {
		this.classLink = classLink;
	}

	public String isIsCourseApproved() {
		return this.isCourseApproved;
	}

	public String getIsCourseApproved() {
		return this.isCourseApproved;
	}

	public void setIsCourseApproved(String isCourseApproved) {
		this.isCourseApproved = isCourseApproved;
	}

	public List<CourseTime> getCourseTime() {
		return this.courseTime;
	}

	public void setCourseTime(List<CourseTime> courseTime) {
		this.courseTime = courseTime;
	}

	public List<Test> getTest() {
		return this.test;
	}

	public void setTest(List<Test> test) {
		this.test = test;
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

	public List<UserInfo> getUserInfo() {
		return this.userInfo;
	}

	public void setUserInfo(List<UserInfo> userInfo) {
		this.userInfo = userInfo;
	}

}


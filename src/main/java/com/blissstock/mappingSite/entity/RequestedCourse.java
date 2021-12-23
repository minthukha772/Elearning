package com.blissstock.mappingSite.entity;

import java.util.ArrayList;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "requested_course")
public class RequestedCourse {
    
    @Column(name = "requested_course_id")
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long requestedCourseId;

	@Column(name = "requested_course_status", length = 10)
  	private String requestedCourseStatus;
	
	@Column(name = "requested_user_name", length = 100)
  	// @NotBlank(message = "Please enter your name")
  	private String requestedUserName;

	  @Column(name = "requested_course_name", length = 100)
  	// @NotBlank(message = "Please enter your name")
  	private String requestedCourseName;

	// @NotBlank(message="Please enter course fees")
	@Column(name="requested_course_fees")
	private int requestedCourseFees;

    //mapping
	// @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "requestedCourse")
    // @JsonIgnore
    // private List<PaymentReceive> paymentReceive = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "uid_fkey")
    @JsonIgnore
    private UserInfo userInfo;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "courseId_fkey")
    @JsonIgnore
    private CourseInfo courseInfo;

	// @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "requestedCourse")
    // @JsonIgnore
    // private List<PaymentReceive> paymentReceive = new ArrayList<>();


	//Constructors


	public RequestedCourse() {
	}

	public RequestedCourse(Long requestedCourseId, String requestedCourseStatus, String requestedUserName, String requestedCourseName, int requestedCourseFees, UserInfo userInfo, CourseInfo courseInfo, List<PaymentReceive> paymentReceive) {
		this.requestedCourseId = requestedCourseId;
		this.requestedCourseStatus = requestedCourseStatus;
		this.requestedUserName = requestedUserName;
		this.requestedCourseName = requestedCourseName;
		this.requestedCourseFees = requestedCourseFees;
		this.userInfo = userInfo;
		this.courseInfo = courseInfo;
		// this.paymentReceive = paymentReceive;
	}
	


	public Long getRequestedCourseId() {
		return this.requestedCourseId;
	}

	public void setRequestedCourseId(Long requestedCourseId) {
		this.requestedCourseId = requestedCourseId;
	}

	public String getRequestedCourseStatus() {
		return this.requestedCourseStatus;
	}

	public void setRequestedCourseStatus(String requestedCourseStatus) {
		this.requestedCourseStatus = requestedCourseStatus;
	}

	public String getRequestedUserName() {
		return this.requestedUserName;
	}

	public void setRequestedUserName(String requestedUserName) {
		this.requestedUserName = requestedUserName;
	}

	public String getRequestedCourseName() {
		return this.requestedCourseName;
	}

	public void setRequestedCourseName(String requestedCourseName) {
		this.requestedCourseName = requestedCourseName;
	}

	public int getRequestedCourseFees() {
		return this.requestedCourseFees;
	}

	public void setRequestedCourseFees(int requestedCourseFees) {
		this.requestedCourseFees = requestedCourseFees;
	}

	public UserInfo getUserInfo() {
		return this.userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	public CourseInfo getCourseInfo() {
		return this.courseInfo;
	}

	public void setCourseInfo(CourseInfo courseInfo) {
		this.courseInfo = courseInfo;
	}

	// public List<PaymentReceive> getPaymentReceive() {
	// 	return this.paymentReceive;
	// }

	// public void setPaymentReceive(List<PaymentReceive> paymentReceive) {
	// 	this.paymentReceive = paymentReceive;
	// }



}

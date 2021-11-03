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

    @Column(name = "about_course", length = 250)
	@NotBlank(message="Please enter about course")
	private String aboutCourse;
	
    @NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name="start_date")
	private Date startDate;

    @NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name="end_date")
	private Date endDate;

    @NotBlank(message="Please enter course fees")
	@Column(name="course_fees")
	private int fees;

    @NotBlank(message="Please enter class link")
    @Column(name = "class_link")
	private String classLink;
	
    @Column(name= "isCourseApproved", nullable = false)
	private boolean isCourseApproved = false;

	//mapping
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy="courseInfo")
	@JsonIgnore
	private List<CourseTime> courseTime= new ArrayList<>();

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy="courseInfo")
	@JsonIgnore
	private List<Syllabus> syllabus= new ArrayList<>();

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
}


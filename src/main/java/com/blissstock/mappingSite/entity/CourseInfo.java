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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
  // @NotBlank(message="Please enter course name")
  private String courseName;

  @Column(name = "class_type", length = 20)
  /// @NotBlank(message="Please enter class type")
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
  // TODO stuNum error
  // @Column(name = "student_num", length = 20)
  // private int stuNum;

  // @Column(name = "course_title", length = 250)
	// // @NotBlank(message="Please enter course title")
	// private String title;

	// @Column(name = "course_content", length = 500)
	// // @NotBlank(message="Please enter course content")
	// private String content;

  @Column(name = "student_num", length = 20)
  private int stuNum;

  //@NotNull
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  @Column(name = "start_date")
  private Date startDate;

  // @NotNull
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  @Column(name = "end_date")
  private Date endDate;

  // @NotBlank(message="Please enter course fees")
  @Column(name = "course_fees")
  private int fees;

  @Column(name = "isCourseApproved", nullable = false)
  private boolean isCourseApproved = false;

  // mapping
  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "courseInfo")
  @JsonIgnore
  private List<CourseTime> courseTime = new ArrayList<>();

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "courseInfo")
  @JsonIgnore
  private List<Syllabus> syllabus = new ArrayList<>();

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "courseInfo")
  @JsonIgnore
  private List<Test> test = new ArrayList<>();

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "courseInfo")
  @JsonIgnore
  private List<JoinCourseUser> join = new ArrayList<>();

  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "uid_fkey")
  @JsonIgnore
  private UserInfo userInfo;

  public void setIsCourseApproved(Boolean isCourseApproved) {
		this.isCourseApproved = isCourseApproved;
	}

  // public String getTitle() {
	// 	return this.title;
	// }

	// public void setTitle(String title) {
	// 	this.title = title;
	// }

}

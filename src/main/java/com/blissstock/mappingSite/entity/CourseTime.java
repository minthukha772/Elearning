package com.blissstock.mappingSite.entity;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "course_time")
public class CourseTime {
	
	@Column(name = "course_time_id")
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long courseTimeId;
	
    @Column(name = "course_days", length = 255)
    @NotBlank(message="Please choose days")
	private String courseDays;

    @Column(name = "course_start_time", columnDefinition= "TIMESTAMP WITH TIME ZONE")
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date courseStartTime;

    @Column(name = "course_end_time", columnDefinition= "TIMESTAMP WITH TIME ZONE")
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date courseEndTime;
	
	//mapping
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "courseId_fkey")
    @JsonIgnore
    private CourseInfo courseInfo;
	
    //Constructors

    public CourseTime() {
    }

    public CourseTime(Long courseTimeId, String courseDays, Date courseStartTime, Date courseEndTime, CourseInfo courseInfo) {
        this.courseTimeId = courseTimeId;
        this.courseDays = courseDays;
        this.courseStartTime = courseStartTime;
        this.courseEndTime = courseEndTime;
        this.courseInfo = courseInfo;
    }

    public Long getCourseTimeId() {
        return this.courseTimeId;
    }

    public void setCourseTimeId(Long courseTimeId) {
        this.courseTimeId = courseTimeId;
    }

    public String getCourseDays() {
        return this.courseDays;
    }

    public void setCourseDays(String courseDays) {
        this.courseDays = courseDays;
    }

    public Date getCourseStartTime() {
        return this.courseStartTime;
    }

    public void setCourseStartTime(Date courseStartTime) {
        this.courseStartTime = courseStartTime;
    }

    public Date getCourseEndTime() {
        return this.courseEndTime;
    }

    public void setCourseEndTime(Date courseEndTime) {
        this.courseEndTime = courseEndTime;
    }

    public CourseInfo getCourseInfo() {
        return this.courseInfo;
    }

    public void setCourseInfo(CourseInfo courseInfo) {
        this.courseInfo = courseInfo;
    }


}


package com.blissstock.mappingSite.entity;
import java.util.Date;

import javax.persistence.*;
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

    @Column(name = "course_start_time")
    @NotNull
    // @Temporal(TemporalType.TIMESTAMP)
    private String courseStartTime;

    @Column(name = "course_end_time")
    @NotNull
    // @Temporal(TemporalType.TIMESTAMP)
    private String courseEndTime;
	
	//mapping
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "courseId_fkey")
    @JsonIgnore
    private CourseInfo courseInfo;
    
    public CourseTime(String courseDays, String courseStartTime, String courseEndTime){
    	this.courseDays = courseDays;
    	this.courseStartTime = courseStartTime;
    	this.courseEndTime = courseEndTime;
    }
	
}


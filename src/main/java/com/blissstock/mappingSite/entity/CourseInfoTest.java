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
@Table(name = "course_info_test")
public class CourseInfoTest {
	
	@Column(name = "course_id")
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long courseId;
	
	@Column(name = "course_name", length = 100)
	//@NotBlank(message="Please enter course name")
	private String courseName;

    @Column(name = "class_type", length = 20)
	//@NotBlank(message="Please enter class type")
	private String classType;

    @Column(name = "category", length = 100)
	//@NotBlank(message="Please choose category")
	private String category;

    @Column(name = "level", length = 15)
	//@NotBlank(message="Please choose course level")
	private String level;

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


    public CourseInfoTest(Long courseId, String courseName, String classType, String category, String level) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.classType = classType;
        this.category = category;
        this.level = level;
    }

    
	
}



package com.blissstock.mappingSite.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "test")
public class Test {
	
	@Column(name = "test_id")
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long testId;

    @NotBlank(message="Please enter test link")
    @Column(name = "test_link")
	private String testLink;

	//mapping
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "courseId_fkey")
    @JsonIgnore
    private CourseInfo courseInfo;
	
    //Constructors

    public Test(Long testId, String testLink, CourseInfo courseInfo) {
        this.testId = testId;
        this.testLink = testLink;
        this.courseInfo = courseInfo;
    }

    public Long getTestId() {
        return this.testId;
    }

    public void setTestId(Long testId) {
        this.testId = testId;
    }

    public String getTestLink() {
        return this.testLink;
    }

    public void setTestLink(String testLink) {
        this.testLink = testLink;
    }

    public CourseInfo getCourseInfo() {
        return this.courseInfo;
    }

    public void setCourseInfo(CourseInfo courseInfo) {
        this.courseInfo = courseInfo;
    }


}


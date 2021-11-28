package com.blissstock.mappingSite.entity;

import java.time.LocalDate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "review")
public class Review {
	
	@Column(name = "review_id")
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long reviewId;
	
    //@NotBlank(message="Please choose review type")
    @Column(name = "review_type")
	private int reviewType;

    //@NotBlank(message="Please fill rating star")
    @Column(name = "star")
	private float star;

    //@NotBlank(message="Please fill feedback")
	@Column(name="feedback")
	private String feedback;

    @NotNull
    @Column(name = "review_status", length = 15)
	private String reviewStatus;

    @Column(name = "assigned_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate assignedDate;
    
	//mapping
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "uid_fkey")
    @JsonIgnore
    private UserInfo userInfo;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "courseId_fkey")
    @JsonIgnore
    private CourseInfo courseInfo;

    public Review() {
    }
   

    public Review(Long reviewId, int reviewType, float star, String feedback, String reviewStatus, LocalDate assignedDate, UserInfo userInfo, CourseInfo courseInfo) {
        this.reviewId = reviewId;
        this.reviewType = reviewType;
        this.star = star;
        this.feedback = feedback;
        this.reviewStatus = reviewStatus;
        this.assignedDate = assignedDate;
        this.userInfo = userInfo;
        this.courseInfo = courseInfo;
    }
    

    public Long getReviewId() {
        return this.reviewId;
    }

    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }

    public int getReviewType() {
        return this.reviewType;
    }

    public void setReviewType(int reviewType) {
        this.reviewType = reviewType;
    }

    public float getStar() {
        return this.star;
    }

    public void setStar(float star) {
        this.star = star;
    }

    public String getFeedback() {
        return this.feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getReviewStatus() {
        return this.reviewStatus;
    }

    public void setReviewStatus(String reviewStatus) {
        this.reviewStatus = reviewStatus;
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
    

    public LocalDate getAssignedDate() {
        return this.assignedDate;
    }

    public void setAssignedDate(LocalDate assignedDate) {
        this.assignedDate = assignedDate;
    }
	
}


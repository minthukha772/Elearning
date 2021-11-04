package com.blissstock.mappingSite.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "review_test")
public class ReviewTest {
	
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

	

    public ReviewTest() {
    }
   
    public ReviewTest(Long reviewId, int reviewType, float f, String feedback, String reviewStatus) {
        this.reviewId = reviewId;
        this.reviewType = reviewType;
        this.star = f;
        this.feedback = feedback;
        this.reviewStatus = reviewStatus;

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

	
}



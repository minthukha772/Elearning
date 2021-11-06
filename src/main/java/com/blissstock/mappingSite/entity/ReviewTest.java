package com.blissstock.mappingSite.entity;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
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

    @Min(value = 1, message = "Please fill rating")
    @Max(value = 5, message = "Please fill rating")
    @Column(name = "star")
	private int star;

    @NotBlank(message="Please fill feedback")
    @NotNull
	@Column(name="feedback")
	private String feedback;

    @NotNull
    @Column(name = "review_status", length = 15)
	private String reviewStatus;

	

    public ReviewTest() {
    }
   
    public ReviewTest(Long reviewId, int reviewType, int star, String feedback, String reviewStatus) {
        this.reviewId = reviewId;
        this.reviewType = reviewType;
        this.star = star;
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

    public int getStar() {
        return this.star;
    }

    public void setStar(int star) {
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



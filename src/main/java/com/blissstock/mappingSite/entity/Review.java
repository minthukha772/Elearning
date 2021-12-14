package com.blissstock.mappingSite.entity;

import java.time.LocalDate;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.blissstock.mappingSite.dto.StudentReviewDTO;
import com.blissstock.mappingSite.dto.TeacherReviewDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "review")
public class Review {
	
	@Column(name = "review_id")
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long reviewId;
   
    // @Min(value = 1, message = "Please fill rating")
    // @Max(value = 5, message = "Please fill rating")
    @Column(name = "star")
	private int star=0;

    @Column(name="review_type")
	private int reviewType=0;

    //@NotBlank(message="Please fill feedback")
	@Column(name="feedback")
	private String feedback;

    @Column(name = "assigned_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date assignedDate;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "join_fkey")
    @JsonIgnore
    private JoinCourseUser join;

    public static Review fromReviewDTO(StudentReviewDTO stuReviewDTO) {
        Review review = new Review();
        review.star= stuReviewDTO.getStar();
        review.feedback= stuReviewDTO.getFeedback();
        review.assignedDate=GregorianCalendar.getInstance().getTime();
        return review;
      }

      public static Review fromTrReviewDTO(TeacherReviewDTO trReviewDTO) {
        Review review = new Review();
        review.feedback= trReviewDTO.getFeedback();
        review.reviewType= trReviewDTO.getReviewType();
        review.assignedDate=GregorianCalendar.getInstance().getTime();
        return review;
      }

}
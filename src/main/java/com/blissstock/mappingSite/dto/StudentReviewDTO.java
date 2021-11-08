package com.blissstock.mappingSite.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Data
public class StudentReviewDTO{

	  private Long reviewId;
	
    //@NotBlank(message="Please choose review type")
	  private int reviewType;

    @Min(value = 1, message = "Please fill rating")
    @Max(value = 5, message = "Please fill rating")
	  private int star;

    @NotBlank(message="Please fill feedback")
	  private String feedback;

	  private String reviewStatus;
}

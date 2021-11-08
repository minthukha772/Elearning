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
public class TeacherReviewDTO{

	private Long reviewId;
	
    @Min(value = 1, message = "Please fill review type")
    @Max(value = 2, message = "Please fill review type")
	private int reviewType;

    @NotBlank(message="Please fill feedback")
	private String feedback;

	  private String reviewStatus;
}

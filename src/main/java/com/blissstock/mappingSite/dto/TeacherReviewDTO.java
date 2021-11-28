package com.blissstock.mappingSite.dto;

import java.util.LinkedHashMap;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import com.blissstock.mappingSite.interfaces.TeacherReview;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter

@Data
public class TeacherReviewDTO implements TeacherReview{

	private Long reviewId;
	
    @Min(value = 1, message = "Please fill review type")
    @Max(value = 2, message = "Please fill review type")
	private int reviewType;

    @NotBlank(message="Please fill feedback")
	private String feedback;

	  private String reviewStatus;

    @Override
	  public LinkedHashMap<String, String> toMapTrReview() {
		LinkedHashMap<String, String> map = new LinkedHashMap<>();
		map.put("reveiw type", this.reviewType + "");
		map.put("feedback", this.feedback);
		
		return map;
	  }
}

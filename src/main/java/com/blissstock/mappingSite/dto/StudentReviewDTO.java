package com.blissstock.mappingSite.dto;

import java.util.LinkedHashMap;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import com.blissstock.mappingSite.interfaces.StudentReview;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class StudentReviewDTO implements StudentReview{

	  private Long reviewId;

    @Min(value = 1, message = "Please fill rating")
    @Max(value = 5, message = "Please fill rating")
	  private int star;

    @NotBlank(message="Please fill feedback")
	  private String feedback;


	  @Override
	  public LinkedHashMap<String, String> toMapReview() {
		LinkedHashMap<String, String> map = new LinkedHashMap<>();
		map.put("star", this.star + "");
		map.put("feedback", this.feedback);
		
		return map;
	  }
}

package com.blissstock.mappingSite.dto;

import java.sql.Date;

import javax.validation.constraints.NotBlank;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class SyllabusDTO {
   
	//private Long courseId;

	//@NotBlank(message="Please fill title")
	private String title;

}

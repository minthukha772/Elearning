package com.blissstock.mappingSite.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseInfoDTO {
    
    public String courseName;

    public String teacherName;

    public LocalDate startDate;

    public LocalDate endDate;
    
}

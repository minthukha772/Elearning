package com.blissstock.mappingSite.model;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherPaymentLists {
    String paymentDate;
    String courseStartDate;
    String courseEndDate;
    String paymentStatus;
    // String userName;
    String teacherName;
    String courseName;
    int courseFees;
    Long teacherId;
    // Long userId;
    // Long courseId;
    String duration;
    int payAmount;


    
    public TeacherPaymentLists(int payAmount,String duration, Long teacherId, String teacherName, String courseName, String courseStartDate, String courseEndDate, String paymentDate, int courseFees, String paymentStatus) {
        this.payAmount = payAmount;
        this.duration = duration;
        this.teacherId = teacherId;
        this.teacherName = teacherName;
        this.courseName = courseName;
        this.courseStartDate = courseStartDate;
        this.courseEndDate = courseEndDate;
        this.paymentDate = paymentDate;
        this.courseFees = courseFees;
        this.paymentStatus = paymentStatus;
        
    }


// public TeacherPaymentLists(String paymentStatus2) {

//     this.paymentStatus = paymentStatus2;

//     }

// public void setDurat(Long teacherId) {
//     this.teacherId = teacherId;
// }

// public Long getTeacherId()  {
//     return this.teacherId;
// }

// public void setTeacherId(Long teacherId) {
//     this.teacherId = teacherId;
// }

// public Long getTeacherId()  {
//     return this.teacherId;
// }

// public void setTeacherName(String teacherName) {
//     this.teacherName = teacherName;
// }

// public String getTeacherName()  {
//     return this.teacherName;
// }

// public void setCourseName(String courseName) {
//     this.courseName = courseName;
// }

// public String getCourseName() {
//     return this.courseName;
// }

// public void setCourseStartDate(String courseStartDate) {
//     this.courseStartDate = courseStartDate;
// }

// public String getCourseStartDate() {
//     return this.courseStartDate;
// }

// public void setCourseEndDate(String courseEndDate) {
//     this.courseEndDate = courseEndDate;
// }

// public String getCourseEndDate() {
//     return this.courseEndDate;
// }

// public void setPaymentDate(String paymentDate) {
//     this.paymentDate = paymentDate;
// }

// public String getPaymentDate() {
//     return this.paymentDate;
// }

// public void setCourseFees(int courseFees) {
//     this.courseFees = courseFees;
// }

// public int getCourseFees() {
//     return this.courseFees;
// }

// public void setPaymentStatus(String paymentStatus) {
//     this.paymentStatus = paymentStatus;
// }

// public String getPaymentStatus() {
//     return this.paymentStatus;
// }



}


package com.blissstock.mappingSite.model;

import java.util.Date;

public class PaymentLists {
    Date paymentDate;
    String paymentStatus;
    String userName;
    String courseName;
    int courseFees;
    Long userId;
    Long courseId;

    public PaymentLists() {
    }
    
        public PaymentLists(Date paymentDate, String paymentStatus, String userName, String courseName, int courseFees, Long userId, Long courseId) {
            this.paymentDate = paymentDate;
            this.paymentStatus = paymentStatus;
            this.userName = userName;
            this.courseName = courseName;
            this.courseFees = courseFees;
            this.userId = userId;
            this.courseId = courseId;
        }
    

    public Date getPaymentDate() {
        return this.paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentStatus() {
        return this.paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCourseName() {
        return this.courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getCourseFees() {
        return this.courseFees;
    }

    public void setCourseFees(int courseFees) {
        this.courseFees = courseFees;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCourseId() {
        return this.courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }
        
    
}


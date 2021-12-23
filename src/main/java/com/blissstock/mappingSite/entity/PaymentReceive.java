package com.blissstock.mappingSite.entity;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "payment_receive")
public class PaymentReceive {
	
	@Column(name = "payment_receive_id")
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long paymentReceiveId;

        //@NotNull
        @Column(name = "slip", nullable = true, length = 64)
        private String slip;
    
        //@NotNull
        @Column(name = "payment_status", length = 15)
        private String paymentStatus;
    
        //@NotNull
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        @Column(name="payment_receive_date")
        private Date paymentReceiveDate = new Date();
    
        @Column(name = "payment_error_status", length = 50)
        private String paymentErrStatus;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "join_fkey")
    @JsonIgnore
    private UserInfo userInfo;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "courseId_fkey")
    @JsonIgnore
    private CourseInfo courseInfo;

    public PaymentReceive() {
    }


    public String getPaymentErrStatus() {
        return this.paymentErrStatus;
    }

    public void setPaymentErrStatus(String paymentErrStatus) {
        this.paymentErrStatus = paymentErrStatus;
    }

    public Long getPaymentReceiveId() {
        return this.paymentReceiveId;
    }

    public void setPaymentReceiveId(Long paymentReceiveId) {
        this.paymentReceiveId = paymentReceiveId;
    }

    public String getSlip() {
        return this.slip;
    }

    public void setSlip(String slip) {
        this.slip = slip;
    }

    public String getPaymentStatus() {
        return this.paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Date getPaymentReceiveDate() {
        return this.paymentReceiveDate;
    }

    public void setPaymentReceiveDate(Date paymentReceiveDate) {
        this.paymentReceiveDate = paymentReceiveDate;
    }

    public UserInfo getUserInfo() {
        return this.userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public CourseInfo getCourseInfo() {
        return this.courseInfo;
    }

    public void setCourseInfo(CourseInfo courseInfo) {
        this.courseInfo = courseInfo;
    }
    @Transient
    public String getSlipImagePath() {
        if (slip == null || paymentReceiveId == null) return null;
         
        return "/slips/" + paymentReceiveId + "/" + slip;
    }

    public PaymentReceive(Long paymentReceiveId, String slip, String paymentStatus, Date paymentReceiveDate, String paymentErrStatus, UserInfo userInfo, CourseInfo courseInfo) {
        this.paymentReceiveId = paymentReceiveId;
        this.slip = slip;
        this.paymentStatus = paymentStatus;
        this.paymentReceiveDate = paymentReceiveDate;
        this.paymentErrStatus = paymentErrStatus;
        this.userInfo = userInfo;
        this.courseInfo = courseInfo;
    }


    

	
	

}


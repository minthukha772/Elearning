package com.blissstock.mappingSite.entity;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "payment_testing")
public class PaymentTesting {
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

    public PaymentTesting() {
    }


    public PaymentTesting(Long paymentReceiveId, String slip, String paymentStatus, Date paymentReceiveDate, String paymentErrStatus) {
        this.paymentReceiveId = paymentReceiveId;
        this.slip = slip;
        this.paymentStatus = paymentStatus;
        this.paymentReceiveDate = paymentReceiveDate;
        this.paymentErrStatus = paymentErrStatus;
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

    public Date getPaymentReceiveDate() {
        return this.paymentReceiveDate;
    }

    public void setPaymentReceiveDate(Date paymentReceiveDate) {
        this.paymentReceiveDate = paymentReceiveDate;
    }

    public String getPaymentStatus() {
        return this.paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
    
    @Transient
    public String getSlipImagePath() {
        if (slip == null || paymentReceiveId == null) return null;
         
        return "/slips/" + paymentReceiveId + "/" + slip;
    }
    
}

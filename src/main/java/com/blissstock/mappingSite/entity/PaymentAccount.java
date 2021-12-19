package com.blissstock.mappingSite.entity;
import java.util.ArrayList;

import javax.persistence.CascadeType;
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


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payment_account")
public class PaymentAccount {
	
	@Column(name = "payment_account_id")
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long paymentAccountId;
	
    @Column(name = "account_name", length = 255)
    //@NotBlank(message="Please fill bank account name")
	private String accountName;

    @Column(name="account_number")
    //@NotBlank(message="Please fill bank account number.")
    private Integer accountNumber;
    
    @Transient
	private Long checkedBank;

	//mapping
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "uid_fkey")
    @JsonIgnore
    private UserInfo userInfo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "bank_id_fkey")
    @JsonIgnore
    private BankInfo bankInfo;
	
    //Constructors

    public PaymentAccount() {
    }

    public PaymentAccount(Long paymentAccountId, String accountName, Integer accountNumber, Long checkedBank, UserInfo userInfo, BankInfo bankInfo) {
        this.paymentAccountId = paymentAccountId;
        this.accountName = accountName;
        this.accountNumber = accountNumber;
        this.checkedBank = checkedBank;
        this.userInfo = userInfo;
        this.bankInfo = bankInfo;
    }

    public Long getPaymentAccountId() {
        return this.paymentAccountId;
    }

    public void setPaymentAccountId(Long paymentAccountId) {
        this.paymentAccountId = paymentAccountId;
    }

    public String getAccountName() {
        return this.accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public Integer getAccountNumber() {
        return this.accountNumber;
    }

    public void setAccountNumber(Integer accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Long getCheckedBank() {
        return this.checkedBank;
    }

    public void setCheckedBank(Long checkedBank) {
        this.checkedBank = checkedBank;
    }

    public UserInfo getUserInfo() {
        return this.userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public BankInfo getBankInfo() {
        return this.bankInfo;
    }

    public void setBankInfo(BankInfo bankInfo) {
        this.bankInfo = bankInfo;
    }

   
}
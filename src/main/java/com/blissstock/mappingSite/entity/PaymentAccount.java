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
	
   
}
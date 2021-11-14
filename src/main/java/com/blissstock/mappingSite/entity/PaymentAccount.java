package com.blissstock.mappingSite.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "payment_account")
public class PaymentAccount {
	
	@Column(name = "payment_account_id")
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long paymentAccountId;
	
    @Column(name = "payment_service_name", length = 255)
    @NotBlank(message="Please fill payment service")
	private String serviceName;

    @Column(name = "account_name", length = 255)
    @NotBlank(message="Please fill bank account name")
	private String accountName;

    @Column(name="account_number")
    @NotBlank(message="Please fill bank account number.")
    private int accountNumber;
    
	//mapping
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "uid_fkey")
    @JsonIgnore
    private UserInfo userInfo;
	
}


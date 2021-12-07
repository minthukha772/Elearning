package com.blissstock.mappingSite.entity;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
	
    @Column(name = "payment_service_name", length = 255)
    //@NotBlank(message="Please fill payment service")
	private String serviceName;

    @Column(name = "account_name", length = 255)
    //@NotBlank(message="Please fill bank account name")
	private String accountName;

    @Column(name="account_number")
    //@NotBlank(message="Please fill bank account number.")
    private Integer accountNumber;
    
	//mapping
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "uid_fkey")
    @JsonIgnore
    private UserInfo userInfo;
	
}


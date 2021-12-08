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
@Table(name = "bank_mst")
public class BankInfo {
    
	@Column(name = "bank_id")
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long bankId;
	
    @Column(name = "bank_name", length = 255)
    //@NotBlank(message="Please fill payment service")
	private String bankName;

}


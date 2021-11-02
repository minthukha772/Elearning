package com.blissstock.mappingSite.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public @Data class TeacherRegisterDTO {

    @Email
    private String email;

    @NotBlank
    private String name;

    @NotBlank
    @Size(min = 8)
    private String password;

    @NotBlank
    @Size(min = 8)
    private String confirmPassword;

    @NotBlank
    private String gender;

    @NotBlank
    private String phone;

    @NotBlank
    private String dob;

    private int zipCode;

    @NotBlank
    private String city;

    @NotBlank
    private String division;

    @NotBlank
    private String address;

    @NotBlank
    private String education;

    @NotBlank
    private String nrc;

    @NotBlank
    private String award;

    @NotBlank
    private String selfDescription;

    private String kPay;
    private String cbPay;
    private String wave;
    private String kbz;
    private String cb;
    private String aya;

    private boolean isAcceptTerm;

}

package com.blissstock.mappingSite.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
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

    @NotBlank(message = "This field is required")
    private String name;

    @NotBlank(message = "This field is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @NotBlank(message = "This field is required")
    @Size(min = 8, message = "Confirm Password must be at least 8 characters")
    private String confirmPassword;

    @NotBlank(message = "This field is required")
    private String gender;

    @NotBlank(message = "This field is required")
    private String phone;

    @NotBlank(message = "This field is required")
    private String dob;

    @Min(value =  10000, message = "invalid format")
    @Max(value =  99999, message = "invalid format")
    private int zipCode;

    @NotBlank(message = "This field is required")
    private String city;

    @NotBlank(message = "This field is required")
    private String division;

    @NotBlank(message = "This field is required")
    private String address;

    @NotBlank(message = "This field is required")
    private String education;

    @NotBlank(message = "This field is required")
    private String nrc;

    @NotBlank(message = "This field is required")
    private String award;

    @NotBlank(message = "This field is required")
    private String selfDescription;

    private String kPay;
    private String cbPay;
    private String wave;
    private String kbz;
    private String cb;
    private String aya;

    private boolean isAcceptTerm;

}

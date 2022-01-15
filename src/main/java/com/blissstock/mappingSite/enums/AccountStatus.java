package com.blissstock.mappingSite.enums;
import lombok.Getter;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum AccountStatus {

    //For Student
    REGISTERED("REGISTERED"),

    //For Teacher
    VERIFIED("VERIFIED"),
    REQUESTED("REQUESTED"),

    //For All User
    SUSPENDED("SUSPENDED");
    
    @Getter private String value;

   

}

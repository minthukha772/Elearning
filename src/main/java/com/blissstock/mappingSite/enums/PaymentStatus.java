package com.blissstock.mappingSite.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum PaymentStatus {
  
    COMPLETE("COMPLETE"),
    PENDING("PENDING"),
    REQUESTED("REQUESTED"),
    ERROR("ERROR");
    
    @Getter private String value;

   

}

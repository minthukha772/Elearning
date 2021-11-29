package com.blissstock.mappingSite.enums;

import lombok.Getter;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum TokenType {
    PASSWORD_RESET("password_reset"),
    VERIFICATION("verification");

    
    @Getter private String value;
}

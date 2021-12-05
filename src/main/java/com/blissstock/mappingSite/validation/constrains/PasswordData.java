package com.blissstock.mappingSite.validation.constrains;

import lombok.Getter;

@Getter
public abstract class PasswordData {
    private String password;
    private String confirmPassword;
    public Object getPassword() {
        return null;
    }
    public Object getConfirmPassword() {
        return null;
    }
}

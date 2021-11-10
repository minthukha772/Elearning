package com.blissstock.mappingSite.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.blissstock.mappingSite.validation.validators.EmailValidator;

import org.junit.jupiter.api.Test;

public class CheckEmailTests {
    @Test
    public void ValidEmailTestCase(){
        EmailValidator emailValidator = new EmailValidator();
        assertEquals(true, emailValidator.validateEmail("email@example.com"));
    }
}

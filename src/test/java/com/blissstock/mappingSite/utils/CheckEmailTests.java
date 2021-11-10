package com.blissstock.mappingSite.utils;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.blissstock.mappingSite.validation.validators.EmailValidator;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class CheckEmailTests {

    @Test
    @Tag("ValidEmail")
    public void ValidEmailTestCase() {
        EmailValidator emailValidator = new EmailValidator();
        assertTrue(emailValidator.validateEmail("email@example.com"));
        assertTrue(emailValidator.validateEmail("firstname.lastname@example.com"));
        assertTrue(emailValidator.validateEmail("email@subdomain.example.com"));
        assertTrue(emailValidator.validateEmail("firstname+lastname@example.com"));
        assertTrue(emailValidator.validateEmail("1234567890@example.com"));
        assertTrue(emailValidator.validateEmail("email@example-one.com"));
        assertTrue(emailValidator.validateEmail("_______@example.com"));
        assertTrue(emailValidator.validateEmail("email@example.name"));
        assertTrue(emailValidator.validateEmail("email@example.museum"));
        assertTrue(emailValidator.validateEmail("email@example.co.jp"));
        assertTrue(emailValidator.validateEmail("firstname-lastname@example.com"));
    }

    @Test
    @Tag("Invalid Email")
    public void invalidEmailTests() {
        EmailValidator emailValidator = new EmailValidator();
        assertFalse(emailValidator.validateEmail("plainaddress"));
        assertFalse(emailValidator.validateEmail("#@%^%#$@#$@#.com"));
        assertFalse(emailValidator.validateEmail("@example.com"));
        assertFalse(emailValidator.validateEmail("Joe Smith <email@example.com>"));
        assertFalse(emailValidator.validateEmail("email.example.com"));
        assertFalse(emailValidator.validateEmail("email@example@example.com"));
        assertFalse(emailValidator.validateEmail(".email@example.com"));
        assertFalse(emailValidator.validateEmail("email.@example.com"));
        assertFalse(emailValidator.validateEmail("email..email@example.com"));
        assertFalse(emailValidator.validateEmail("あいうえお@example.com"));
        assertFalse(emailValidator.validateEmail("email@example.com (Joe Smith)"));
        assertFalse(emailValidator.validateEmail("email@example"));
        assertFalse(emailValidator.validateEmail("email@111.222.333.44444"));
        assertFalse(emailValidator.validateEmail("email@example..com"));
        assertFalse(emailValidator.validateEmail("Abc..123@example.com"));
    }
}

package com.blissstock.mappingSite.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.blissstock.mappingSite.dto.UserRegisterDTO;
import com.github.javafaker.Faker;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class UserRegisterValidatorTest {

    private static Validator validator;
    private UserRegisterDTO userTemplate;

    @BeforeAll
    public static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();


    }

    @BeforeEach
    public void setUpUserTemplate() {

        Faker faker = new Faker();
        userTemplate = new UserRegisterDTO();
        userTemplate.setEmail(faker.bothify("????##@gmail.com"));
        userTemplate.setName(faker.name().fullName());
        userTemplate.setPassword(faker.bothify("????####"));
        userTemplate.setConfirmPassword(userTemplate.getPassword());
        userTemplate.setGender("Male");
        userTemplate.setPhone(faker.phoneNumber().phoneNumber());
        userTemplate.setDob(faker.date().birthday(13, 60));
        userTemplate.setZipCode(faker.number().numberBetween(0, 99999));
        userTemplate.setCity(faker.address().city());
        userTemplate.setDivision(faker.address().state());
        userTemplate.setAddress(faker.address().fullAddress());
        userTemplate.setEducation(faker.educator().university());
        userTemplate.setAcceptTerm(true);
    }

    @Test
    @Tag("validTest")
    public void validTest() {
        Set<ConstraintViolation<UserRegisterDTO>> constraintViolations = validator.validate(userTemplate);
        assertEquals(0, constraintViolations.size());
    }

    @Test
    @Tag("emptyEmail")
    public void emptyEmailTest() {
        userTemplate.setEmail("");
        Set<ConstraintViolation<UserRegisterDTO>> constraintViolations = validator.validate(userTemplate);
        assertEquals(1, constraintViolations.size());
        assertEquals("Invalid email",constraintViolations.iterator().next().getMessage());
    }

    @Test
    @Tag("invalidEmail")
    public void invalidEmailTest() {
        userTemplate.setEmail("bsbsbs");
        Set<ConstraintViolation<UserRegisterDTO>> constraintViolations = validator.validate(userTemplate);
        assertEquals(1, constraintViolations.size());
        assertEquals("Invalid email",constraintViolations.iterator().next().getMessage());
    }

    @Test
    @Tag("emptyPassword")
    public void emptyPasswordTest() {
        userTemplate.setPassword("");
        Set<ConstraintViolation<UserRegisterDTO>> constraintViolations = validator.validate(userTemplate);
        System.out.println(userTemplate);

        List<String> messages = constraintViolations.stream().map(e -> e.getMessage()).collect(Collectors.toList());
        System.out.println(messages);
        
        assertEquals(3, constraintViolations.size());
        assertTrue(messages.size() == 3);
        assertTrue(messages.contains("The passwords must be match"));
        //assertTrue(messages.contains());
        assertTrue(messages.contains("password must be at least 8 characters"));
        
    }


}

package com.blissstock.mappingSite.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class EmailCheckRegisterDTO {
    @Email
    @NotBlank(message = "Email must not be blank")
    private String email;

    @NotBlank
    private String role;
}

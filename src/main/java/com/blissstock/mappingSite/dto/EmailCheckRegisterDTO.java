package com.blissstock.mappingSite.dto;

import javax.validation.constraints.NotBlank;

import com.blissstock.mappingSite.validation.constrains.ValidEmail;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class EmailCheckRegisterDTO {
    @ValidEmail
    @NotBlank(message = "Email must not be blank")
    private String email;

    @NotBlank
    private String role;
}

package com.blissstock.mappingSite.dto;

import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class PaymentInfoDTO {
    @NotBlank
    private String serviceName;
    private String accountName;
    private String accountNumber;
}

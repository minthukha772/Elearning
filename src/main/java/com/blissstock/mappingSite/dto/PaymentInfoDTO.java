package com.blissstock.mappingSite.dto;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentInfoDTO {
    @NotBlank
    private String serviceName;
    private String accountName;
    private String accountNumber;

    //Constructors

    public PaymentInfoDTO() {
    }

    public PaymentInfoDTO(String serviceName, String accountName, String accountNumber) {
        this.serviceName = serviceName;
        this.accountName = accountName;
        this.accountNumber = accountNumber;
    }

    public String getServiceName() {
        return this.serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getAccountName() {
        return this.accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNumber() {
        return this.accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

}

package com.blissstock.mappingSite.dto;

import com.blissstock.mappingSite.entity.PaymentAccount;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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

  @NotNull
  PaymentAccount primaryAccount;

  @NotNull
  PaymentAccount secondaryAccount;
}

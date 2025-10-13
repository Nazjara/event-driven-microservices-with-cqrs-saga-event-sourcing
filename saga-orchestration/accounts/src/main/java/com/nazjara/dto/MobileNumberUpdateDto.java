package com.nazjara.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class MobileNumberUpdateDto {

  @NotEmpty(message = "Customer Id can not be a null or empty")
  private String customerId;

  @NotEmpty(message = "Account number can not be a null or empty")
  private Long accountNumber;

  @NotEmpty(message = "Loan number can not be a null or empty")
  private Long loanNumber;

  @NotEmpty(message = "Card number can not be a null or empty")
  private Long cardNumber;

  @NotEmpty(message = "Current mobile Number can not be a null or empty")
  @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile Number must be 10 digits")
  private String currentMobileNumber;

  @NotEmpty(message = "New mobile Number can not be a null or empty")
  @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile Number must be 10 digits")
  private String newMobileNumber;
}

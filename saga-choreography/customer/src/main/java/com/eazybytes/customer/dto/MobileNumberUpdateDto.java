package com.eazybytes.customer.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class MobileNumberUpdateDto {

  @NotEmpty(message = "Current mobile number can not be a null or empty")
  @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
  private String currentMobileNumber;

  @NotEmpty(message = "New mobile number can not be a null or empty")
  @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
  private String newMobileNumber;
}

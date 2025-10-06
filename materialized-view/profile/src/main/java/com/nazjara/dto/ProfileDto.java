package com.nazjara.dto;

import lombok.Data;

@Data
public class ProfileDto {

  private String name;
  private String mobileNumber;
  private String accountNumber;
  private boolean cardNumber;
  private long loanNumber;
}

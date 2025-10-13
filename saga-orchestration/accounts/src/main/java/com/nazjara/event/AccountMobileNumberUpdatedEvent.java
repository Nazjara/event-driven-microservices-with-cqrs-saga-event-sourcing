package com.nazjara.event;

import lombok.Data;

@Data
public class AccountMobileNumberUpdatedEvent {

  private String customerId;
  private Long accountNumber;
  private Long loanNumber;
  private Long cardNumber;
  private String currentMobileNumber;
  private String newMobileNumber;
}

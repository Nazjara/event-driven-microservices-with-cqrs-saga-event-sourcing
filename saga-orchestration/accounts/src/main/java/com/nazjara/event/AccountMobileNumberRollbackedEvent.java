package com.nazjara.event;

import lombok.Data;

@Data
public class AccountMobileNumberRollbackedEvent {

  private String customerId;
  private Long accountNumber;
  private String currentMobileNumber;
  private String newMobileNumber;
  private String errorMessage;
}

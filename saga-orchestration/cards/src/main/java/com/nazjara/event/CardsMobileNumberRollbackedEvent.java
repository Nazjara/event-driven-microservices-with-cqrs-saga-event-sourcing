package com.nazjara.event;

import lombok.Data;

@Data
public class CardsMobileNumberRollbackedEvent {

  private String customerId;
  private Long accountNumber;
  private Long cardNumber;
  private String currentMobileNumber;
  private String newMobileNumber;
  private String errorMessage;
}

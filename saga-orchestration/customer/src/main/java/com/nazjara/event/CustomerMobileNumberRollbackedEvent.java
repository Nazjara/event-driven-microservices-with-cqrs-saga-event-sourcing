package com.nazjara.event;

import lombok.Data;

@Data
public class CustomerMobileNumberRollbackedEvent {

  private String customerId;
  private String currentMobileNumber;
  private String newMobileNumber;
  private final String errorMessage;
}

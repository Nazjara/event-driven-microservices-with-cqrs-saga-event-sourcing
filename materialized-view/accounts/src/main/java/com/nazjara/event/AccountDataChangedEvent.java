package com.nazjara.event;

import lombok.Data;

@Data
public class AccountDataChangedEvent {

  private String mobileNumber;
  private String accountNumber;
}

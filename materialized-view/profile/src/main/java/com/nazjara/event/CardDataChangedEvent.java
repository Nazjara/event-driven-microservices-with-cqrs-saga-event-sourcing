package com.nazjara.event;

import lombok.Data;

@Data
public class CardDataChangedEvent {

  private String mobileNumber;
  private Long cardNumber;
}

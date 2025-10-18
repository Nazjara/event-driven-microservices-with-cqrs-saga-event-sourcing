package com.nazjara.command;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@Builder
public class RollbackCardMobileNumberCommand {

  @TargetAggregateIdentifier
  private final Long cardNumber;

  private final String customerId;
  private final Long accountNumber;
  private final String currentMobileNumber;
  private final String newMobileNumber;
  private final String errorMessage;
}

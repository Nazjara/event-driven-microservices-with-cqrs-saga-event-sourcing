package com.nazjara.command;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@Builder
public class UpdateCardMobileNumberCommand {

  @TargetAggregateIdentifier
  private Long cardNumber;

  private String customerId;
  private Long accountNumber;
  private Long loanNumber;
  private String currentMobileNumber;
  private String newMobileNumber;
}

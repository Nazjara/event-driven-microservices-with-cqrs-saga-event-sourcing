package com.nazjara.command;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@Builder
public class UpdateAccountMobileNumberCommand {

  @TargetAggregateIdentifier
  private Long accountNumber;

  private String customerId;
  private Long loanNumber;
  private Long cardNumber;
  private String currentMobileNumber;
  private String newMobileNumber;
}

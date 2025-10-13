package com.nazjara.command;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@Builder
public class UpdateCustomerMobileNumberCommand {

  @TargetAggregateIdentifier
  private String customerId;

  private Long accountNumber;
  private Long loanNumber;
  private Long cardNumber;
  private String currentMobileNumber;
  private String newMobileNumber;
}

package com.nazjara.command;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@Builder
public class RollbackAccountMobileNumberCommand {

  @TargetAggregateIdentifier
  private final Long accountNumber;

  private final String customerId;
  private final String currentMobileNumber;
  private final String newMobileNumber;
  private final String errorMessage;
}

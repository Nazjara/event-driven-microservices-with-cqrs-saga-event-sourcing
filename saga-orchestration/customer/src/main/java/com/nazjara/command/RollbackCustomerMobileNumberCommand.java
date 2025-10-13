package com.nazjara.command;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@Builder
public class RollbackCustomerMobileNumberCommand {

  @TargetAggregateIdentifier
  private String customerId;

  private String currentMobileNumber;
  private String newMobileNumber;
  private final String errorMessage;
}

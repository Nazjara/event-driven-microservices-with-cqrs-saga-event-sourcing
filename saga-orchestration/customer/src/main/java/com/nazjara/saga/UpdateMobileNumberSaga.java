package com.nazjara.saga;

import com.nazjara.command.RollbackAccountMobileNumberCommand;
import com.nazjara.command.RollbackCardMobileNumberCommand;
import com.nazjara.command.RollbackCustomerMobileNumberCommand;
import com.nazjara.command.UpdateAccountMobileNumberCommand;
import com.nazjara.command.UpdateCardMobileNumberCommand;
import com.nazjara.command.UpdateLoanMobileNumberCommand;
import com.nazjara.event.AccountMobileNumberRollbackedEvent;
import com.nazjara.event.AccountMobileNumberUpdatedEvent;
import com.nazjara.event.CardsMobileNumberRollbackedEvent;
import com.nazjara.event.CardsMobileNumberUpdatedEvent;
import com.nazjara.event.CustomerMobileNumberRollbackedEvent;
import com.nazjara.event.CustomerMobileNumberUpdatedEvent;
import com.nazjara.event.LoanMobileNumberUpdatedEvent;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;

@Saga
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMobileNumberSaga {

  private transient CommandGateway commandGateway;

  @SagaEventHandler(associationProperty = "customerId")
  @StartSaga
  public void handle(CustomerMobileNumberUpdatedEvent event) {
    log.info("Starting Saga for CustomerMobileNumberUpdatedEvent for customerId: {}",
        event.getCustomerId());

    var updateAccountMobileNumberCommand = UpdateAccountMobileNumberCommand.builder()
        .accountNumber(event.getAccountNumber())
        .customerId(event.getCustomerId())
        .loanNumber(event.getLoanNumber())
        .cardNumber(event.getCardNumber())
        .currentMobileNumber(event.getCurrentMobileNumber())
        .newMobileNumber(event.getNewMobileNumber())
        .build();

    commandGateway.send(updateAccountMobileNumberCommand,
        (commandMessage, commandResultMessage) -> {
          if (commandResultMessage.isExceptional()) {
            log.error("Error updating account mobile number for customerId: {}",
                event.getCustomerId(), commandResultMessage.exceptionResult());
            var rollbackCustomerMobileNumberCommand = RollbackCustomerMobileNumberCommand.builder()
                .customerId(event.getCustomerId())
                .currentMobileNumber(event.getNewMobileNumber())
                .newMobileNumber(event.getCurrentMobileNumber())
                .errorMessage(commandResultMessage.exceptionResult().getMessage())
                .build();

            commandGateway.sendAndWait(rollbackCustomerMobileNumberCommand);
          }
        });
  }

  @SagaEventHandler(associationProperty = "customerId")
  public void handle(AccountMobileNumberUpdatedEvent event) {
    log.info("Processing Saga for AccountMobileNumberUpdatedEvent for accountNumber: {}",
        event.getAccountNumber());

    var updateCardMobileNumberCommand = UpdateCardMobileNumberCommand.builder()
        .accountNumber(event.getAccountNumber())
        .customerId(event.getCustomerId())
        .loanNumber(event.getLoanNumber())
        .cardNumber(event.getCardNumber())
        .currentMobileNumber(event.getCurrentMobileNumber())
        .newMobileNumber(event.getNewMobileNumber())
        .build();

    commandGateway.send(updateCardMobileNumberCommand,
        (commandMessage, commandResultMessage) -> {
          if (commandResultMessage.isExceptional()) {
            log.error("Error updating card mobile number for customerId: {}",
                event.getCustomerId(), commandResultMessage.exceptionResult());
            var rollbackAccountMobileNumberCommand = RollbackAccountMobileNumberCommand.builder()
                .customerId(event.getCustomerId())
                .accountNumber(event.getAccountNumber())
                .currentMobileNumber(event.getNewMobileNumber())
                .newMobileNumber(event.getCurrentMobileNumber())
                .errorMessage(commandResultMessage.exceptionResult().getMessage())
                .build();

            commandGateway.sendAndWait(rollbackAccountMobileNumberCommand);
          }
        });
  }

  @SagaEventHandler(associationProperty = "customerId")
  public void handle(CardsMobileNumberUpdatedEvent event) {
    log.info("Processing Saga for CardsMobileNumberUpdatedEvent for cardNumber: {}",
        event.getCardNumber());

    var updateLoanMobileNumberCommand = UpdateLoanMobileNumberCommand.builder()
        .accountNumber(event.getAccountNumber())
        .customerId(event.getCustomerId())
        .loanNumber(event.getLoanNumber())
        .cardNumber(event.getCardNumber())
        .currentMobileNumber(event.getCurrentMobileNumber())
        .newMobileNumber(event.getNewMobileNumber())
        .build();

    commandGateway.send(updateLoanMobileNumberCommand,
        (commandMessage, commandResultMessage) -> {
          if (commandResultMessage.isExceptional()) {
            log.error("Error updating loan mobile number for customerId: {}",
                event.getCustomerId(), commandResultMessage.exceptionResult());
            var rollbackCardMobileNumberCommand = RollbackCardMobileNumberCommand.builder()
                .customerId(event.getCustomerId())
                .accountNumber(event.getAccountNumber())
                .cardNumber(event.getCardNumber())
                .currentMobileNumber(event.getNewMobileNumber())
                .newMobileNumber(event.getCurrentMobileNumber())
                .errorMessage(commandResultMessage.exceptionResult().getMessage())
                .build();

            commandGateway.sendAndWait(rollbackCardMobileNumberCommand);
          }
        });
  }

  @SagaEventHandler(associationProperty = "customerId")
  @EndSaga
  public void handle(LoanMobileNumberUpdatedEvent event) {
    log.info("Ending Saga for LoanMobileNumberUpdatedEvent for loanNumber: {}",
        event.getLoanNumber());
  }

  @SagaEventHandler(associationProperty = "customerId")
  public void handle(CardsMobileNumberRollbackedEvent event) {
    log.info("Processing Saga for CardsMobileNumberRollbackedEvent for cardNumber: {}",
        event.getCardNumber());

    var rollbackAccountMobileNumberCommand = RollbackAccountMobileNumberCommand.builder()
        .accountNumber(event.getAccountNumber())
        .customerId(event.getCustomerId())
        .currentMobileNumber(event.getCurrentMobileNumber())
        .newMobileNumber(event.getNewMobileNumber())
        .errorMessage(event.getErrorMessage())
        .build();

    commandGateway.send(rollbackAccountMobileNumberCommand);
  }

  @SagaEventHandler(associationProperty = "customerId")
  public void handle(AccountMobileNumberRollbackedEvent event) {
    log.info("Processing Saga for AccountMobileNumberRollbackedEvent for accountNumber: {}",
        event.getAccountNumber());

    var rollbackCustomerMobileNumberCommand = RollbackCustomerMobileNumberCommand.builder()
        .customerId(event.getCustomerId())
        .currentMobileNumber(event.getCurrentMobileNumber())
        .newMobileNumber(event.getNewMobileNumber())
        .errorMessage(event.getErrorMessage())
        .build();

    commandGateway.send(rollbackCustomerMobileNumberCommand);
  }

  @SagaEventHandler(associationProperty = "customerId")
  @EndSaga
  public void handle(CustomerMobileNumberRollbackedEvent event) {
    log.info("Ending Saga for CustomerMobileNumberRollbackedEvent for customerId: {}",
        event.getCustomerId());
  }
}

package com.nazjara.command.aggregate;

import com.nazjara.command.CreateAccountCommand;
import com.nazjara.command.DeleteAccountCommand;
import com.nazjara.command.UpdateAccountCommand;
import com.nazjara.event.AccountCreatedEvent;
import com.nazjara.event.AccountDeletedEvent;
import com.nazjara.event.AccountUpdatedEvent;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

@Aggregate
@NoArgsConstructor
public class AccountAggregate {

  @AggregateIdentifier
  private Long accountNumber;

  private String mobileNumber;
  private String accountType;
  private String branchAddress;
  private boolean activeSw;

  @CommandHandler
  public AccountAggregate(CreateAccountCommand command) {
    var event = new AccountCreatedEvent();
    BeanUtils.copyProperties(command, event);
    AggregateLifecycle.apply(event);
  }

  @EventSourcingHandler
  public void on(AccountCreatedEvent event) {
    this.accountNumber = event.getAccountNumber();
    this.branchAddress = event.getBranchAddress();
    this.accountType = event.getAccountType();
    this.mobileNumber = event.getMobileNumber();
    this.activeSw = event.isActiveSw();
  }

  @CommandHandler
  public void handleUpdate(UpdateAccountCommand command, EventStore eventStore) {
//    var commands = eventStore.readEvents(command.getCustomerId()).asStream() .toList();
//    if (commands.isEmpty()) {
//      throw new ResourceNotFoundException("Customer", "mobileNumber",
//          command.getMobileNumber());
//    }

    var event = new AccountUpdatedEvent();
    BeanUtils.copyProperties(command, event);
    AggregateLifecycle.apply(event);
  }

  @EventSourcingHandler
  public void on(AccountUpdatedEvent event) {
    this.accountType = event.getAccountType();
    this.branchAddress = event.getBranchAddress();
  }

  @CommandHandler
  public void handleDelete(DeleteAccountCommand command) {
    var event = new AccountDeletedEvent();
    BeanUtils.copyProperties(command, event);
    AggregateLifecycle.apply(event);
  }

  @EventSourcingHandler
  public void on(AccountDeletedEvent event) {
    this.activeSw = event.isActiveSw();
  }
}

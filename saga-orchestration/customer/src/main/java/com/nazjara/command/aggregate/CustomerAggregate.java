package com.nazjara.command.aggregate;

import com.nazjara.command.CreateCustomerCommand;
import com.nazjara.command.DeleteCustomerCommand;
import com.nazjara.command.RollbackCustomerMobileNumberCommand;
import com.nazjara.command.UpdateCustomerCommand;
import com.nazjara.command.UpdateCustomerMobileNumberCommand;
import com.nazjara.event.CustomerCreatedEvent;
import com.nazjara.event.CustomerDeletedEvent;
import com.nazjara.event.CustomerMobileNumberRollbackedEvent;
import com.nazjara.event.CustomerMobileNumberUpdatedEvent;
import com.nazjara.event.CustomerUpdatedEvent;
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
public class CustomerAggregate {

  @AggregateIdentifier
  private String customerId;

  private String name;
  private String email;
  private String mobileNumber;
  private boolean activeSw;
  private String errorMessage;

  @CommandHandler
  public CustomerAggregate(CreateCustomerCommand command) {
    var event = new CustomerCreatedEvent();
    BeanUtils.copyProperties(command, event);
    AggregateLifecycle.apply(event);
  }

  @EventSourcingHandler
  public void on(CustomerCreatedEvent event) {
    this.customerId = event.getCustomerId();
    this.name = event.getName();
    this.email = event.getEmail();
    this.mobileNumber = event.getMobileNumber();
    this.activeSw = event.isActiveSw();
  }

  @CommandHandler
  public void handleUpdate(UpdateCustomerCommand command, EventStore eventStore) {
//    var commands = eventStore.readEvents(command.getCustomerId()).asStream() .toList();
//    if (commands.isEmpty()) {
//      throw new ResourceNotFoundException("Customer", "mobileNumber",
//          command.getMobileNumber());
//    }

    var event = new CustomerUpdatedEvent();
    BeanUtils.copyProperties(command, event);
    AggregateLifecycle.apply(event);
  }

  @EventSourcingHandler
  public void on(CustomerUpdatedEvent event) {
    this.name = event.getName();
    this.email = event.getEmail();
  }

  @CommandHandler
  public void handleDelete(DeleteCustomerCommand command) {
    var event = new CustomerDeletedEvent();
    BeanUtils.copyProperties(command, event);
    AggregateLifecycle.apply(event);
  }

  @EventSourcingHandler
  public void on(CustomerDeletedEvent event) {
    this.activeSw = event.isActiveSw();
  }

  @CommandHandler
  public void handleUpdateMobileNumber(UpdateCustomerMobileNumberCommand command) {
    var event = new CustomerMobileNumberUpdatedEvent();
    BeanUtils.copyProperties(command, event);
    AggregateLifecycle.apply(event);
  }

  @EventSourcingHandler
  public void on(CustomerMobileNumberUpdatedEvent event) {
    this.mobileNumber = event.getNewMobileNumber();
  }

  @CommandHandler
  public void handleRollbackMobileNumber(RollbackCustomerMobileNumberCommand command) {
    var event = new CustomerMobileNumberRollbackedEvent();
    BeanUtils.copyProperties(command, event);
    AggregateLifecycle.apply(event);
  }

  @EventSourcingHandler
  public void on(CustomerMobileNumberRollbackedEvent event) {
    this.mobileNumber = event.getCurrentMobileNumber();
    this.errorMessage = event.getErrorMessage();
  }
}

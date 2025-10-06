package com.nazjara.command.aggregate;

import com.nazjara.command.CreateCustomerCommand;
import com.nazjara.command.DeleteCustomerCommand;
import com.nazjara.command.UpdateCustomerCommand;
import com.nazjara.event.CustomerCreatedEvent;
import com.nazjara.event.CustomerDataChangedEvent;
import com.nazjara.event.CustomerDeletedEvent;
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

  @CommandHandler
  public CustomerAggregate(CreateCustomerCommand command) {
    var event = new CustomerCreatedEvent();
    BeanUtils.copyProperties(command, event);

    var customerDataChangedEvent = new CustomerDataChangedEvent();
    BeanUtils.copyProperties(command, customerDataChangedEvent);

    AggregateLifecycle.apply(event).andThen(() ->
        AggregateLifecycle.apply(customerDataChangedEvent));
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
  public void handleUpdate(UpdateCustomerCommand command) {
    var event = new CustomerUpdatedEvent();
    BeanUtils.copyProperties(command, event);
    var customerDataChangedEvent = new CustomerDataChangedEvent();
    BeanUtils.copyProperties(command, customerDataChangedEvent);
    AggregateLifecycle.apply(event);
    AggregateLifecycle.apply(customerDataChangedEvent);
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
}

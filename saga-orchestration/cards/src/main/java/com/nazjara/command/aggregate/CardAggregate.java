package com.nazjara.command.aggregate;

import com.nazjara.command.RollbackCardMobileNumberCommand;
import com.nazjara.command.UpdateCardMobileNumberCommand;
import com.nazjara.event.CardsMobileNumberRollbackedEvent;
import com.nazjara.event.CardsMobileNumberUpdatedEvent;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

@Aggregate
@NoArgsConstructor
public class CardAggregate {

  @AggregateIdentifier
  private Long cardNumber;

  private String mobileNumber;
  private String cardType;
  private int totalLimit;
  private int amountUsed;
  private int availableAmount;
  private boolean activeSw;
  private String errorMessage;

  @CommandHandler
  public void handleUpdateMobileNumber(UpdateCardMobileNumberCommand command) {
    var event = new CardsMobileNumberUpdatedEvent();
    BeanUtils.copyProperties(command, event);
    AggregateLifecycle.apply(event);
  }

  @EventSourcingHandler
  public void on(CardsMobileNumberUpdatedEvent event) {
    this.mobileNumber = event.getNewMobileNumber();
  }

  @CommandHandler
  public void handleRollbackMobileNumber(RollbackCardMobileNumberCommand command) {
    var event = new CardsMobileNumberRollbackedEvent();
    BeanUtils.copyProperties(command, event);
    AggregateLifecycle.apply(event);
  }

  @EventSourcingHandler
  public void on(CardsMobileNumberRollbackedEvent event) {
    this.mobileNumber = event.getCurrentMobileNumber();
    this.errorMessage = event.getErrorMessage();
  }
}

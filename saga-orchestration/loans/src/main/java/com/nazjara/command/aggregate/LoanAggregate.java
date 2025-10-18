package com.nazjara.command.aggregate;

import com.nazjara.command.UpdateLoanMobileNumberCommand;
import com.nazjara.event.LoanMobileNumberUpdatedEvent;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

@Aggregate
@NoArgsConstructor
public class LoanAggregate {

  private Long loanNumber;
  private String mobileNumber;
  private String loanType;
  private int totalLoan;
  private int amountPaid;
  private int outstandingAmount;
  private boolean activeSw;

  @CommandHandler
  public void handleUpdateMobileNumber(UpdateLoanMobileNumberCommand command) {
    var event = new LoanMobileNumberUpdatedEvent();
    BeanUtils.copyProperties(command, event);
    AggregateLifecycle.apply(event);
  }

  @EventSourcingHandler
  public void on(LoanMobileNumberUpdatedEvent event) {
    this.mobileNumber = event.getNewMobileNumber();
  }
}

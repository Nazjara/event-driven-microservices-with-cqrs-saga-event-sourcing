package com.nazjara.projection;

import com.nazjara.event.LoanMobileNumberUpdatedEvent;
import com.nazjara.service.ILoansService;
import lombok.RequiredArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ProcessingGroup("card-group")
public class LoanProjection {

  private final ILoansService service;

  @EventHandler
  public void on(LoanMobileNumberUpdatedEvent event) {
    service.updateMobileNumber(event.getCurrentMobileNumber(), event.getNewMobileNumber());
  }
}

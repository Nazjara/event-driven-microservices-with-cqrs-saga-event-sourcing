package com.nazjara.query.projection;

import com.nazjara.event.CardsMobileNumberRollbackedEvent;
import com.nazjara.event.CardsMobileNumberUpdatedEvent;
import com.nazjara.service.ICardsService;
import lombok.RequiredArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ProcessingGroup("card-group")
public class CardProjection {

  private final ICardsService service;

  @EventHandler
  public void on(CardsMobileNumberUpdatedEvent event) {
    service.updateMobileNumber(event.getCurrentMobileNumber(), event.getNewMobileNumber());
  }

  @EventHandler
  public void on(CardsMobileNumberRollbackedEvent event) {
    service.updateMobileNumber(event.getNewMobileNumber(), event.getCurrentMobileNumber());
  }
}

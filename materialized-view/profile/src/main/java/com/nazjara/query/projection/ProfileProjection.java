package com.nazjara.query.projection;

import com.nazjara.event.AccountDataChangedEvent;
import com.nazjara.event.CustomerDataChangedEvent;
import com.nazjara.service.IProfileService;
import lombok.RequiredArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ProcessingGroup("profile-group")
public class ProfileProjection {

  private final IProfileService service;

  @EventHandler
  public void on(CustomerDataChangedEvent event) {
    service.handleCustomerDataChangedEvent(event);
  }

  @EventHandler
  public void on(AccountDataChangedEvent event) {
    service.handleAccountDataChangedEvent(event);
  }
}

package com.nazjara.query.projection;

import com.nazjara.entity.Accounts;
import com.nazjara.event.AccountCreatedEvent;
import com.nazjara.event.AccountDeletedEvent;
import com.nazjara.event.AccountMobileNumberRollbackedEvent;
import com.nazjara.event.AccountMobileNumberUpdatedEvent;
import com.nazjara.event.AccountUpdatedEvent;
import com.nazjara.service.IAccountsService;
import lombok.RequiredArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ProcessingGroup("accounts-group")
public class AccountProjection {

  private final IAccountsService service;

  @EventHandler
  public void on(AccountCreatedEvent event) {
    var accounts = new Accounts();
    BeanUtils.copyProperties(event, accounts);
    service.createAccount(accounts);
  }

  @EventHandler
  public void on(AccountUpdatedEvent event) {
    service.updateAccount(event);
  }

  @EventHandler
  public void on(AccountDeletedEvent event) {
    service.deleteAccount(event.getAccountNumber());
  }

  @EventHandler
  public void on(AccountMobileNumberUpdatedEvent event) {
    service.updateMobileNumber(event.getCurrentMobileNumber(), event.getNewMobileNumber());
  }

  @EventHandler
  public void on(AccountMobileNumberRollbackedEvent event) {
    service.updateMobileNumber(event.getNewMobileNumber(), event.getCurrentMobileNumber());
  }
}

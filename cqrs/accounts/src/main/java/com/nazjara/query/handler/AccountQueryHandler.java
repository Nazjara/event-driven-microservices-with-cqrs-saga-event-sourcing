package com.nazjara.query.handler;

import com.nazjara.dto.AccountsDto;
import com.nazjara.query.FindAccountQuery;
import com.nazjara.service.IAccountsService;
import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountQueryHandler {

  private final IAccountsService service;

  @QueryHandler
  public AccountsDto findAccount(FindAccountQuery query) {
    return service.fetchAccount(query.getMobileNumber());
  }
}

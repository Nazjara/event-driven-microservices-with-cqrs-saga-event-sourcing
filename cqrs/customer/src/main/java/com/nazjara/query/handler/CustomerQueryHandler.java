package com.nazjara.query.handler;

import com.nazjara.dto.CustomerDto;
import com.nazjara.query.FindCustomerQuery;
import com.nazjara.service.ICustomerService;
import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerQueryHandler {

  private final ICustomerService service;

  @QueryHandler
  public CustomerDto findCustomer(FindCustomerQuery query) {
    return service.fetchCustomer(query.getMobileNumber());
  }
}

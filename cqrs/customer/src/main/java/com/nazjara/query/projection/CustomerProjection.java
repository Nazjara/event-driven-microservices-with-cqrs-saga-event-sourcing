package com.nazjara.query.projection;

import com.nazjara.entity.Customer;
import com.nazjara.event.CustomerCreatedEvent;
import com.nazjara.event.CustomerDeletedEvent;
import com.nazjara.event.CustomerUpdatedEvent;
import com.nazjara.service.ICustomerService;
import lombok.RequiredArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ProcessingGroup("customer-group")
public class CustomerProjection {

  private final ICustomerService service;

  @EventHandler
  public void on(CustomerCreatedEvent event) {
    var customer = new Customer();
    BeanUtils.copyProperties(event, customer);
    service.createCustomer(customer);
  }

  @EventHandler
  public void on(CustomerUpdatedEvent event) {
    service.updateCustomer(event);
  }

  @EventHandler
  public void on(CustomerDeletedEvent event) {
    service.deleteCustomer(event.getCustomerId());
  }
}

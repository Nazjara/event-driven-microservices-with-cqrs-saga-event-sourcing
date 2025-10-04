package com.nazjara.mapper;

import com.nazjara.dto.CustomerDto;
import com.nazjara.entity.Customer;
import com.nazjara.event.CustomerUpdatedEvent;

public class CustomerMapper {

  public static CustomerDto mapToCustomerDto(Customer customer, CustomerDto customerDto) {
    customerDto.setCustomerId(customer.getCustomerId());
    customerDto.setName(customer.getName());
    customerDto.setEmail(customer.getEmail());
    customerDto.setMobileNumber(customer.getMobileNumber());
    customerDto.setActiveSw(customer.isActiveSw());
    return customerDto;
  }

  public static Customer mapToCustomer(CustomerDto customerDto, Customer customer) {
    customer.setCustomerId(customerDto.getCustomerId());
    customer.setName(customerDto.getName());
    customer.setEmail(customerDto.getEmail());
    customer.setMobileNumber(customerDto.getMobileNumber());
    if (customerDto.isActiveSw()) {
      customer.setActiveSw(customerDto.isActiveSw());
    }
    return customer;
  }
  public static Customer mapEventToCustomer(CustomerUpdatedEvent event, Customer customer) {
    customer.setName(event.getName());
    customer.setEmail(event.getEmail());

    return customer;
  }
}

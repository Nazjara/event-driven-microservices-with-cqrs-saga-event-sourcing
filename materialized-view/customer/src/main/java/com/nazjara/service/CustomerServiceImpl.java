package com.nazjara.service;

import com.nazjara.constants.CustomerConstants;
import com.nazjara.dto.CustomerDto;
import com.nazjara.entity.Customer;
import com.nazjara.event.CustomerDataChangedEvent;
import com.nazjara.exception.CustomerAlreadyExistsException;
import com.nazjara.exception.ResourceNotFoundException;
import com.nazjara.mapper.CustomerMapper;
import com.nazjara.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements ICustomerService {

  private CustomerRepository customerRepository;
  private EventGateway eventGateway;

  @Override
  public void createCustomer(CustomerDto customerDto) {
    customerDto.setActiveSw(CustomerConstants.ACTIVE_SW);
    var customer = CustomerMapper.mapToCustomer(customerDto, new Customer());
    var optionalCustomer = customerRepository.findByMobileNumberAndActiveSw(
        customerDto.getMobileNumber(), true);

    if (optionalCustomer.isPresent()) {
      throw new CustomerAlreadyExistsException(
          "Customer already registered with given mobileNumber "
              + customerDto.getMobileNumber());
    }

    customerRepository.save(customer);
  }

  @Override
  public CustomerDto fetchCustomer(String mobileNumber) {
    var customer = customerRepository.findByMobileNumberAndActiveSw(mobileNumber, true)
        .orElseThrow(
            () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );

    var customerDto = CustomerMapper.mapToCustomerDto(customer, new CustomerDto());
    return customerDto;
  }

  @Override
  public boolean updateCustomer(CustomerDto customerDto) {
    var customer = customerRepository.findByMobileNumberAndActiveSw(
            customerDto.getMobileNumber(), true)
        .orElseThrow(() -> new ResourceNotFoundException("Customer", "mobileNumber",
            customerDto.getMobileNumber()));

    CustomerMapper.mapToCustomer(customerDto, customer);
    customerRepository.save(customer);
    return true;
  }

  @Override
  public boolean deleteCustomer(String customerId) {
    var customer = customerRepository.findById(customerId).orElseThrow(
        () -> new ResourceNotFoundException("Customer", "customerId", customerId)
    );

    customer.setActiveSw(CustomerConstants.IN_ACTIVE_SW);
    customerRepository.save(customer);

    var event = new CustomerDataChangedEvent();
    event.setMobileNumber(customer.getMobileNumber());
    event.setActiveSw(CustomerConstants.IN_ACTIVE_SW);
    eventGateway.publish(event);

    return true;
  }

}

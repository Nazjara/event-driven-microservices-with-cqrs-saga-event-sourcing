package com.nazjara.service;

import com.nazjara.constants.CustomerConstants;
import com.nazjara.dto.CustomerDto;
import com.nazjara.entity.Customer;
import com.nazjara.event.CustomerUpdatedEvent;
import com.nazjara.exception.CustomerAlreadyExistsException;
import com.nazjara.exception.ResourceNotFoundException;
import com.nazjara.mapper.CustomerMapper;
import com.nazjara.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements ICustomerService {

  private CustomerRepository customerRepository;

  @Override
  public void createCustomer(Customer customer) {
    customer.setActiveSw(CustomerConstants.ACTIVE_SW);
    var optionalCustomer = customerRepository.findByMobileNumberAndActiveSw(
        customer.getMobileNumber(), true);

    if (optionalCustomer.isPresent()) {
      throw new CustomerAlreadyExistsException(
          "Customer already registered with given mobileNumber "
              + customer.getMobileNumber());
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
  public boolean updateCustomer(CustomerUpdatedEvent event) {
    var customer = customerRepository.findByMobileNumberAndActiveSw(
            event.getMobileNumber(), true)
        .orElseThrow(() -> new ResourceNotFoundException("Customer", "mobileNumber",
            event.getMobileNumber()));

    CustomerMapper.mapEventToCustomer(event, customer);
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
    return true;
  }

}

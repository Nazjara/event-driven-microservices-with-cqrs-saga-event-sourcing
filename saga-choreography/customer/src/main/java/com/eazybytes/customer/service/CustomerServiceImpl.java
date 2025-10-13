package com.eazybytes.customer.service;

import com.eazybytes.customer.constants.CustomerConstants;
import com.eazybytes.customer.dto.CustomerDto;
import com.eazybytes.customer.dto.MobileNumberUpdateDto;
import com.eazybytes.customer.entity.Customer;
import com.eazybytes.customer.exception.CustomerAlreadyExistsException;
import com.eazybytes.customer.exception.ResourceNotFoundException;
import com.eazybytes.customer.mapper.CustomerMapper;
import com.eazybytes.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements ICustomerService {

  private final CustomerRepository customerRepository;
  private final StreamBridge streamBridge;

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
    return true;
  }

  @Override
  @Transactional
  public boolean updateMobileNumber(MobileNumberUpdateDto mobileNumberUpdateDto) {
    var mobileNumber = mobileNumberUpdateDto.getCurrentMobileNumber();
    var customer = customerRepository.findByMobileNumberAndActiveSw(mobileNumber, true)
        .orElseThrow(
            () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );

    customer.setMobileNumber(mobileNumberUpdateDto.getNewMobileNumber());
    updateAccountMobileNumber(mobileNumberUpdateDto);
    return true;
  }

  @Override
  @Transactional
  public void rollbackMobileNumberUpdate(MobileNumberUpdateDto mobileNumberUpdateDto) {
    var mobileNumber = mobileNumberUpdateDto.getNewMobileNumber();
    var customer = customerRepository.findByMobileNumberAndActiveSw(mobileNumber, true)
        .orElseThrow(
            () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );

    customer.setMobileNumber(mobileNumberUpdateDto.getCurrentMobileNumber());
  }

  private void updateAccountMobileNumber(MobileNumberUpdateDto mobileNumberUpdateDto) {
    log.info("Sending message to update account mobile number. Details: {}", mobileNumberUpdateDto);
    var result = streamBridge.send("updateAccountMobileNumber-out-0", mobileNumberUpdateDto);
    log.info("Message sent to update account mobile number. Result successful: {}", result);
  }
}

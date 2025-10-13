package com.eazybytes.customer.service;

import com.eazybytes.customer.dto.CustomerDto;
import com.eazybytes.customer.dto.MobileNumberUpdateDto;

public interface ICustomerService {

  /**
   * @param customerDto - CustomerDto Object
   */
  void createCustomer(CustomerDto customerDto);

  /**
   * @param mobileNumber - Input Mobile Number
   * @return Accounts Details based on a given mobileNumber
   */
  CustomerDto fetchCustomer(String mobileNumber);

  /**
   * @param customerDto - CustomerDto Object
   * @return boolean indicating if the update of Account details is successful or not
   */
  boolean updateCustomer(CustomerDto customerDto);

  /**
   * @param customerId - Input Customer ID
   * @return boolean indicating if the delete of Customer details is successful or not
   */
  boolean deleteCustomer(String customerId);

  /**
   * Updates the mobile number for an existing customer.
   *
   * @param mobileNumberUpdateDto the DTO containing the current and new mobile numbers
   * @return a boolean indicating whether the update operation was successful
   */
  boolean updateMobileNumber(MobileNumberUpdateDto mobileNumberUpdateDto);

  /**
   * Rolls back the update of a customer's mobile number. This method reverts any changes that were made
   * during the mobile number update process if an issue occurs.
   *
   * @param mobileNumberUpdateDto a data transfer object containing the current and new mobile numbers
   */
  void rollbackMobileNumberUpdate(MobileNumberUpdateDto mobileNumberUpdateDto);
}

package com.nazjara.service;

import com.nazjara.dto.CustomerDto;
import com.nazjara.entity.Customer;
import com.nazjara.event.CustomerUpdatedEvent;

public interface ICustomerService {

  /**
   * @param customer - Customer Object
   */
  void createCustomer(Customer customer);

  /**
   * @param mobileNumber - Input Mobile Number
   * @return Accounts Details based on a given mobileNumber
   */
  CustomerDto fetchCustomer(String mobileNumber);

  /**
   * @param event - CustomerUpdatedEvent Object
   * @return boolean indicating if the update of Account details is successful or not
   */
  boolean updateCustomer(CustomerUpdatedEvent event);

  /**
   * @param customerId - Input Customer ID
   * @return boolean indicating if the delete of Customer details is successful or not
   */
  boolean deleteCustomer(String customerId);

  /**
   * Updates the mobile number of an existing customer.
   *
   * @param mobileNumber the current mobile number of the customer to be updated
   * @param newMobileNumber the new mobile number to replace the current one
   * @return true if the mobile number update is successful, false otherwise
   */
  boolean updateMobileNumber(String mobileNumber, String newMobileNumber);
}

package com.nazjara.service;

import com.nazjara.dto.ProfileDto;
import com.nazjara.event.AccountDataChangedEvent;
import com.nazjara.event.CustomerDataChangedEvent;

public interface IProfileService {

  /**
   * Retrieves the profile information associated with the given mobile number.
   * This method fetches profile data if the profile is active; otherwise, it throws a ResourceNotFoundException.
   *
   * @param mobileNumber the mobile number linked to the desired profile. Must be 10 digits in length.
   * @return an object of type ProfileDto containing the profile details such as customer ID, name, email, mobile number, and status.
   *         Returns a valid profile only if it is marked as active.
   */
  ProfileDto fetchProfile(String mobileNumber);

  /**
   * Handles events related to changes in customer data. This method processes
   * instances of {@code CustomerDataChangedEvent} to reflect updates in the corresponding profile.
   *
   * @param event the event carrying details about the changed customer data,
   *              including the customer's name, mobile number, and active status.
   */
  void handleCustomerDataChangedEvent(CustomerDataChangedEvent event);

  /**
   * Handles events related to changes in account data. This method updates the profile
   * data associated with the provided account event details to ensure the profile reflects the latest account information.
   *
   * @param event the event carrying details about the changed account data,
   *              including the associated mobile number and account number.
   */
  void handleAccountDataChangedEvent(AccountDataChangedEvent event);
}

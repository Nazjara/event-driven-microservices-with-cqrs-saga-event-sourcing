package com.nazjara.service;

import com.nazjara.dto.AccountsDto;
import com.nazjara.dto.MobileNumberUpdateDto;

/**
 * Interface for Account-related operations
 */
public interface IAccountsService {

  /**
   *
   * @param mobileNumber - Input Mobile Number
   */
  void createAccount(String mobileNumber);

  /**
   *
   * @param mobileNumber - Input Mobile Number
   * @return Accounts Details based on a given mobileNumber
   */
  AccountsDto fetchAccount(String mobileNumber);

  /**
   *
   * @param accountsDto - AccountsDto Object
   * @return boolean indicating if the update of Account details is successful or not
   */
  boolean updateAccount(AccountsDto accountsDto);

  /**
   *
   * @param accountNumber - Input Account Number
   * @return boolean indicating if the delete of Account details is successful or not
   */
  boolean deleteAccount(Long accountNumber);

  /**
   * Updates the mobile number associated with an account.
   *
   * @param mobileNumberUpdateDto an object containing the current mobile number and the new mobile number
   */
  void updateMobileNumber(MobileNumberUpdateDto mobileNumberUpdateDto);

  /**
   * Reverts any updates made to an account's mobile number, restoring the previous mobile number.
   *
   * @param mobileNumberUpdateDto an object containing both the current mobile number
   *                              and the new mobile number that was set, which needs to be rolled back
   */
  void rollbackMobileNumberUpdate(MobileNumberUpdateDto mobileNumberUpdateDto);
}

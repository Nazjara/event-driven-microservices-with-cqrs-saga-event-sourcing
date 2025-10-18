package com.nazjara.service;

import com.nazjara.dto.AccountsDto;
import com.nazjara.entity.Accounts;
import com.nazjara.event.AccountUpdatedEvent;

/**
 * Interface for Account-related operations
 */
public interface IAccountsService {

  /**
   * Creates a new account in the system.
   *
   * @param accounts the account data to be created, including details
   *                 such as mobile number, account type, branch address, etc.
   */
  void createAccount(Accounts accounts);

  /**
   *
   * @param mobileNumber - Input Mobile Number
   * @return Accounts Details based on a given mobileNumber
   */
  AccountsDto fetchAccount(String mobileNumber);

  /**
   * Updates the account details in the system based on the provided event data.
   *
   * @param event the AccountUpdatedEvent object containing updated account details
   *              such as account number, mobile number, account type, branch address,
   *              and active/inactive status.
   * @return true if the account details were successfully updated; false otherwise
   */
  boolean updateAccount(AccountUpdatedEvent event);

  /**
   *
   * @param accountNumber - Input Account Number
   * @return boolean indicating if the delete of Account details is successful or not
   */
  boolean deleteAccount(Long accountNumber);


  /**
   * Updates the mobile number of an existing account.
   *
   * @param oldMobileNumber the current mobile number associated with the account
   * @param newMobileNumber the new mobile number to update the account with
   * @return true if the mobile number was successfully updated, false otherwise
   */
  boolean updateMobileNumber(String oldMobileNumber, String newMobileNumber);
}

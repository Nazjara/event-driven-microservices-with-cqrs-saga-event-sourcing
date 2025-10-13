package com.nazjara.service;

import com.nazjara.dto.LoansDto;
import com.nazjara.dto.MobileNumberUpdateDto;

public interface ILoansService {

  /**
   * @param mobileNumber - Mobile Number of the Customer
   */
  void createLoan(String mobileNumber);

  /**
   * @param mobileNumber - Input mobile Number
   * @return Loan Details based on a given mobileNumber
   */
  LoansDto fetchLoan(String mobileNumber);

  /**
   * @param loansDto - LoansDto Object
   * @return boolean indicating if the update of card details is successful or not
   */
  boolean updateLoan(LoansDto loansDto);

  /**
   * @param mobileNumber - Input Mobile Number
   * @return boolean indicating if the delete of loan details is successful or not
   */
  boolean deleteLoan(Long mobileNumber);

  /**
   * Updates the mobile number of the customer.
   *
   * @param mobileNumberUpdateDto - Data transfer object containing the current mobile number
   *                                and the new mobile number to be updated.
   */
  void updateMobileNumber(MobileNumberUpdateDto mobileNumberUpdateDto);
}

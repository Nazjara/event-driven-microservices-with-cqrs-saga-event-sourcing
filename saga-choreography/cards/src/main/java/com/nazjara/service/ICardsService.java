package com.nazjara.service;

import com.nazjara.dto.CardsDto;
import com.nazjara.dto.MobileNumberUpdateDto;

public interface ICardsService {

  /**
   *
   * @param mobileNumber - Mobile Number of the Customer
   */
  void createCard(String mobileNumber);

  /**
   *
   * @param mobileNumber - Input mobile Number
   * @return Card Details based on a given mobileNumber
   */
  CardsDto fetchCard(String mobileNumber);

  /**
   *
   * @param cardsDto - CardsDto Object
   * @return boolean indicating if the update of card details is successful or not
   */
  boolean updateCard(CardsDto cardsDto);

  /**
   *
   * @param cardNumber - Input Card Number
   * @return boolean indicating if the delete of card details is successful or not
   */
  boolean deleteCard(Long cardNumber);

  /**
   * Updates the mobile number associated with the user's card details.
   *
   * @param mobileNumberUpdateDto - MobileNumberUpdateDto object containing the current mobile number
   *                                and the new mobile number of the user.
   */
  void updateMobileNumber(MobileNumberUpdateDto mobileNumberUpdateDto);

  /**
   * Rolls back an update to the mobile number associated with a card account.
   * This method is intended for use in compensating transactions to restore
   * the previous mobile number if an update fails or is canceled.
   *
   * @param mobileNumberUpdateDto - Data Transfer Object containing the current mobile number and the new mobile number
   *                                involved in the update transaction.
   */
  void rollbackMobileNumberUpdate(MobileNumberUpdateDto mobileNumberUpdateDto);
}

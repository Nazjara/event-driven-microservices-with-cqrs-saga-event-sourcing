package com.nazjara.service;

import com.nazjara.dto.CardsDto;

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
   * Updates the mobile number associated with a card.
   *
   * @param oldMobileNumber the current mobile number to be updated
   * @param newMobileNumber the new mobile number to associate with the card
   * @return boolean indicating whether the update operation was successful or not
   */
  boolean updateMobileNumber(String oldMobileNumber, String newMobileNumber);
}

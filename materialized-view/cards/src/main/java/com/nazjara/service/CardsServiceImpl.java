package com.nazjara.service;

import com.nazjara.constant.CardsConstants;
import com.nazjara.dto.CardsDto;
import com.nazjara.entity.Cards;
import com.nazjara.exception.CardAlreadyExistsException;
import com.nazjara.exception.ResourceNotFoundException;
import com.nazjara.mapper.CardsMapper;
import com.nazjara.repository.CardsRepository;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardsServiceImpl implements ICardsService {

  private final CardsRepository cardsRepository;

  /**
   * @param mobileNumber - Mobile Number of the Customer
   */
  @Override
  public void createCard(String mobileNumber) {
    var optionalCard = cardsRepository.findByMobileNumberAndActiveSw(mobileNumber,
        CardsConstants.ACTIVE_SW);

    if (optionalCard.isPresent()) {
      throw new CardAlreadyExistsException(
          "Card already registered with given mobileNumber " + mobileNumber);
    }
    cardsRepository.save(createNewCard(mobileNumber));
  }

  /**
   * @param mobileNumber - Mobile Number of the Customer
   * @return the new card details
   */
  private Cards createNewCard(String mobileNumber) {
    var newCard = new Cards();
    long randomCardNumber = 100000000000L + new Random().nextInt(900000000);
    newCard.setCardNumber(randomCardNumber);
    newCard.setMobileNumber(mobileNumber);
    newCard.setCardType(CardsConstants.CREDIT_CARD);
    newCard.setTotalLimit(CardsConstants.NEW_CARD_LIMIT);
    newCard.setAmountUsed(0);
    newCard.setAvailableAmount(CardsConstants.NEW_CARD_LIMIT);
    newCard.setActiveSw(CardsConstants.ACTIVE_SW);
    return newCard;
  }

  /**
   * @param mobileNumber - Input mobile Number
   * @return Card Details based on a given mobileNumber
   */
  @Override
  public CardsDto fetchCard(String mobileNumber) {
    var card = cardsRepository.findByMobileNumberAndActiveSw(mobileNumber, CardsConstants.ACTIVE_SW)
        .orElseThrow(() -> new ResourceNotFoundException("Card", "mobileNumber", mobileNumber)
        );
    return CardsMapper.mapToCardsDto(card, new CardsDto());
  }

  /**
   * @param cardsDto - CardsDto Object
   * @return boolean indicating if the update of card details is successful or not
   */
  @Override
  public boolean updateCard(CardsDto cardsDto) {
    var card = cardsRepository.findByMobileNumberAndActiveSw(cardsDto.getMobileNumber(),
            CardsConstants.ACTIVE_SW)
        .orElseThrow(() -> new ResourceNotFoundException("Card", "CardNumber",
            cardsDto.getCardNumber().toString()));
    CardsMapper.mapToCards(cardsDto, card);
    cardsRepository.save(card);
    return true;
  }

  /**
   * @param cardNumber - Input Card Number
   * @return boolean indicating if the delete of card details is successful or not
   */
  @Override
  public boolean deleteCard(Long cardNumber) {
    var card = cardsRepository.findById(cardNumber)
        .orElseThrow(
            () -> new ResourceNotFoundException("Card", "cardNumber", cardNumber.toString())
        );
    card.setActiveSw(CardsConstants.IN_ACTIVE_SW);
    cardsRepository.save(card);
    return true;
  }
}

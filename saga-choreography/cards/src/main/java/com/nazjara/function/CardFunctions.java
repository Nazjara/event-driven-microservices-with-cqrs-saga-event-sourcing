package com.nazjara.function;

import com.nazjara.dto.MobileNumberUpdateDto;
import com.nazjara.service.ICardsService;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class CardFunctions {

  private final StreamBridge streamBridge;
  private final ICardsService cardsService;

  /**
   * Updates the mobile number associated with a card by utilizing the provided card service.
   * <p>
   * The method name must match the binding name defined in the application's properties under
   * `spring.cloud.stream.bindings.{BINDING_NAME}-in-0` and the function name specified under
   * `spring.cloud.function.definition`.
   *
   * @param cardsService the card service responsible for managing card-related operations
   * @return a Consumer that accepts a {@link MobileNumberUpdateDto} to perform the update operation
   */
  @Bean
  public Consumer<MobileNumberUpdateDto> updateCardMobileNumber(ICardsService cardsService) {
    return mobileNumberUpdateDto -> {
      try {
        log.info("Processing message: {}", mobileNumberUpdateDto);
        cardsService.updateMobileNumber(mobileNumberUpdateDto);
        log.info("Message processed successfully: {}", mobileNumberUpdateDto);
      } catch (Exception e) {
        log.error("Error processing message: {}", mobileNumberUpdateDto, e);
        rollbackAccountMobileNumber(mobileNumberUpdateDto);
      }
    };
  }

  /**
   * Handles the rollback of a mobile number update operation for a card.
   * This method is intended for use in compensating transactions
   * to restore the previous state of the card's mobile number.
   * It invokes the rollback operation in the provided card service and initiates
   * a rollback for the associated account's mobile number.
   *
   * @param cardsService the service responsible for managing card operations, including rollback functionality
   * @return a Consumer that processes a {@link MobileNumberUpdateDto} object to perform the rollback operation
   */
  @Bean
  public Consumer<MobileNumberUpdateDto> rollbackCardMobileNumber(ICardsService cardsService) {
    return mobileNumberUpdateDto -> {
      try {
        log.info("Processing message: {}", mobileNumberUpdateDto);
        cardsService.rollbackMobileNumberUpdate(mobileNumberUpdateDto);
        rollbackAccountMobileNumber(mobileNumberUpdateDto);
        log.info("Message processed successfully: {}", mobileNumberUpdateDto);
      } catch (Exception e) {
        log.error("Error processing message: {}", mobileNumberUpdateDto, e);
        // Critical: compensation failed - needs manual intervention or retry
      }
    };
  }

  private void rollbackAccountMobileNumber(MobileNumberUpdateDto mobileNumberUpdateDto) {
    log.info("Sending message to rollback account mobile number. Details: {}",
        mobileNumberUpdateDto);
    try {
      var result = streamBridge.send("rollbackAccountMobileNumber-out-0", mobileNumberUpdateDto);
      if (result) {
        log.info("Message sent to rollback account mobile number");

      } else {
        log.error("Failed to send rollback message - message broker might be unavailable");
        // Critical: compensation failed - needs manual intervention or retry
      }
    } catch (Exception e) {
      log.error("Failed to rollback account mobile number", e);
      // Critical: compensation failed - needs manual intervention or retry
    }
  }
}

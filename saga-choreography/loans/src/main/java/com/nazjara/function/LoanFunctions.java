package com.nazjara.function;

import com.nazjara.dto.MobileNumberUpdateDto;
import com.nazjara.service.ILoansService;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class LoanFunctions {

  private final StreamBridge streamBridge;

  /**
   * Provides a Consumer to update the mobile number associated with a loan.
   * <p>
   * The method name must match the binding name defined in the application's properties under
   * `spring.cloud.stream.bindings.{BINDING_NAME}-in-0` and the function name specified under
   * `spring.cloud.function.definition`.
   *
   * @param loansService the loan service implementation to be used for updating the mobile number
   * @return a Consumer that accepts a MobileNumberUpdateDto object and delegates the update
   * operation to the provided ILoansService implementation
   */
  @Bean
  public Consumer<MobileNumberUpdateDto> updateLoanMobileNumber(ILoansService loansService) {
    return mobileNumberUpdateDto -> {
      try {
        log.info("Processing message: {}", mobileNumberUpdateDto);
        loansService.updateMobileNumber(mobileNumberUpdateDto);
        log.info("Message processed successfully: {}", mobileNumberUpdateDto);
      } catch (Exception e) {
        log.error("Error processing message: {}", mobileNumberUpdateDto, e);
        rollbackCardMobileNumber(mobileNumberUpdateDto);
      }
    };
  }

  private void rollbackCardMobileNumber(MobileNumberUpdateDto mobileNumberUpdateDto) {
    log.info("Sending message to rollback card mobile number. Details: {}",
        mobileNumberUpdateDto);
    try {
      var result = streamBridge.send("rollbackCardMobileNumber-out-0", mobileNumberUpdateDto);
      if (result) {
        log.info("Message sent to rollback card mobile number");

      } else {
        log.error("Failed to send rollback message - message broker might be unavailable");
        // Critical: compensation failed - needs manual intervention or retry
      }
    } catch (Exception e) {
      log.error("Failed to rollback card mobile number", e);
      // Critical: compensation failed - needs manual intervention or retry
    }
  }
}

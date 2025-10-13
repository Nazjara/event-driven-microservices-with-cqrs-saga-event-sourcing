package com.nazjara.function;

import com.nazjara.dto.MobileNumberUpdateDto;
import com.nazjara.service.IAccountsService;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class AccountFunctions {

  private final StreamBridge streamBridge;

  /**
   * Handles the mobile number update operation for an account.
   * The method is designed to be invoked as a message consumer in a Spring Cloud Stream
   * application. It processes incoming instances of {@link MobileNumberUpdateDto}.
   * <p>
   * The method name must match the binding name defined in the application's properties
   * under `spring.cloud.stream.bindings.{BINDING_NAME}-in-0` and the function name specified
   * under `spring.cloud.function.definition`.
   *
   * @return A {@link Consumer} that processes {@link MobileNumberUpdateDto} instances.
   */
  @Bean
  public Consumer<MobileNumberUpdateDto> updateAccountMobileNumber(IAccountsService accountsService) {
    return mobileNumberUpdateDto -> {
      try {
        log.info("Processing message: {}", mobileNumberUpdateDto);
        accountsService.updateMobileNumber(mobileNumberUpdateDto);
        log.info("Message processed successfully: {}", mobileNumberUpdateDto);
      } catch (Exception e) {
        log.error("Error processing message: {}", mobileNumberUpdateDto, e);
        rollbackCustomerMobileNumber(mobileNumberUpdateDto);
      }
    };
  }

  @Bean
  public Consumer<MobileNumberUpdateDto> rollbackAccountMobileNumber(IAccountsService accountsService) {
    return mobileNumberUpdateDto -> {
      try {
        log.info("Processing message: {}", mobileNumberUpdateDto);
        accountsService.rollbackMobileNumberUpdate(mobileNumberUpdateDto);
        rollbackCustomerMobileNumber(mobileNumberUpdateDto);
        log.info("Message processed successfully: {}", mobileNumberUpdateDto);
      } catch (Exception e) {
        log.error("Error processing message: {}", mobileNumberUpdateDto, e);
        // Critical: compensation failed - needs manual intervention or retry
      }
    };
  }

  private void rollbackCustomerMobileNumber(MobileNumberUpdateDto mobileNumberUpdateDto) {
    log.info("Sending message to rollback customer mobile number. Details: {}",
        mobileNumberUpdateDto);
    try {
      var result = streamBridge.send("rollbackCustomerMobileNumber-out-0", mobileNumberUpdateDto);
      if (result) {
        log.info("Message sent to rollback customer mobile number");

      } else {
        log.error("Failed to send rollback message - message broker might be unavailable");
        // Critical: compensation failed - needs manual intervention or retry
      }
    } catch (Exception e) {
      log.error("Failed to rollback customer mobile number", e);
      // Critical: compensation failed - needs manual intervention or retry
    }
  }
}

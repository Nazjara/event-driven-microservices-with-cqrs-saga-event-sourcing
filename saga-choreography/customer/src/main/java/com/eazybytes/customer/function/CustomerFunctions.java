package com.eazybytes.customer.function;

import com.eazybytes.customer.dto.MobileNumberUpdateDto;
import com.eazybytes.customer.service.ICustomerService;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class CustomerFunctions {

  /**
   * Registers a Consumer bean that processes rollback requests for a customer's mobile number update.
   * The consumer takes a MobileNumberUpdateDto object containing the current and new mobile numbers
   * and invokes the rollbackMobileNumberUpdate method in the ICustomerService.
   *
   * @param customerService the service used to handle customer-related operations, including rolling
   *                         back a mobile number update.
   * @return a Consumer that processes MobileNumberUpdateDto objects for rolling back mobile number updates.
   */
  @Bean
  public Consumer<MobileNumberUpdateDto> rollbackCustomerMobileNumber(
      ICustomerService customerService) {
    return mobileNumberUpdateDto -> {
      log.info("Processing message: {}", mobileNumberUpdateDto);
      customerService.rollbackMobileNumberUpdate(mobileNumberUpdateDto);
      log.info("Message processed successfully: {}", mobileNumberUpdateDto);
    };
  }
}

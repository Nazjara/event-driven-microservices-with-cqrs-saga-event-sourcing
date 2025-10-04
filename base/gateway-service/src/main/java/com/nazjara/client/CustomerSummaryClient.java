package com.nazjara.client;

import com.nazjara.dto.AccountsDto;
import com.nazjara.dto.CardsDto;
import com.nazjara.dto.CustomerDto;
import com.nazjara.dto.LoansDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import reactor.core.publisher.Mono;

public interface CustomerSummaryClient {

  @GetExchange(value = "/bank/customer/api/fetch", accept = "application/json")
  Mono<ResponseEntity<CustomerDto>> fetchCustomerDetails(
      @RequestParam("mobileNumber") String mobileNumber);

  @GetExchange(value = "/bank/accounts/api/fetch", accept = "application/json")
  Mono<ResponseEntity<AccountsDto>> fetchAccountDetails(
      @RequestParam("mobileNumber") String mobileNumber);

  @GetExchange(value = "/bank/loans/api/fetch", accept = "application/json")
  Mono<ResponseEntity<LoansDto>> fetchLoanDetails(
      @RequestParam("mobileNumber") String mobileNumber);

  @GetExchange(value = "/bank/cards/api/fetch", accept = "application/json")
  Mono<ResponseEntity<CardsDto>> fetchCardDetails(
      @RequestParam("mobileNumber") String mobileNumber);
}

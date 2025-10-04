package com.nazjara.handler;

import com.nazjara.client.CustomerSummaryClient;
import com.nazjara.dto.CustomerSummaryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CustomerCompositeHandler {

  private final CustomerSummaryClient customerSummaryClient;

  public Mono<ServerResponse> fetchCustomerSummary(ServerRequest serverRequest) {
    var mobileNumber = serverRequest.queryParam("mobileNumber").get();
    var customerDetails = customerSummaryClient.fetchCustomerDetails(mobileNumber);
    var accountDetails = customerSummaryClient.fetchAccountDetails(mobileNumber);
    var loanDetails = customerSummaryClient.fetchLoanDetails(mobileNumber);
    var cardDetails = customerSummaryClient.fetchCardDetails(mobileNumber);

    return Mono.zip(customerDetails, accountDetails, loanDetails, cardDetails)
        .flatMap(tuple -> {
          var customerDto = tuple.getT1().getBody();
          var accountsDto = tuple.getT2().getBody();
          var loansDto = tuple.getT3().getBody();
          var cardsDto = tuple.getT4().getBody();
          var customerSummaryDto = new CustomerSummaryDto(customerDto, accountsDto, loansDto,
              cardsDto);
          return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
              .body(BodyInserters.fromValue(customerSummaryDto));
        });
  }

}

package com.nazjara.controller;

import com.nazjara.command.CreateCustomerCommand;
import com.nazjara.command.DeleteCustomerCommand;
import com.nazjara.command.UpdateCustomerCommand;
import com.nazjara.constants.CustomerConstants;
import com.nazjara.dto.CustomerDto;
import com.nazjara.dto.ResponseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
@RequiredArgsConstructor
public class CustomerCommandController {

  private final CommandGateway commandGateway;

  @PostMapping("/create")
  public ResponseEntity<ResponseDto> createCustomer(@Valid @RequestBody CustomerDto customerDto) {
    var command = CreateCustomerCommand.builder()
        .customerId(UUID.randomUUID().toString())
        .name(customerDto.getName())
        .email(customerDto.getEmail())
        .mobileNumber(customerDto.getMobileNumber())
        .activeSw(CustomerConstants.ACTIVE_SW)
        .build();

    commandGateway.sendAndWait(command);

    return ResponseEntity
        .status(org.springframework.http.HttpStatus.CREATED)
        .body(new ResponseDto(CustomerConstants.STATUS_201, CustomerConstants.MESSAGE_201));
  }

  @PutMapping("/update")
  public ResponseEntity<ResponseDto> updateCustomerDetails(
      @Valid @RequestBody CustomerDto customerDto) {
    var command = UpdateCustomerCommand.builder()
        .customerId(customerDto.getCustomerId())
        .name(customerDto.getName())
        .email(customerDto.getEmail())
        .mobileNumber(customerDto.getMobileNumber())
        .activeSw(CustomerConstants.ACTIVE_SW)
        .build();

    commandGateway.sendAndWait(command);

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(new ResponseDto(CustomerConstants.STATUS_200, CustomerConstants.MESSAGE_200));
  }

  @DeleteMapping("/delete")
  public ResponseEntity<ResponseDto> deleteCustomer(@RequestParam("customerId")
  @Pattern(regexp = "(^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$)",
      message = "CustomerId is invalid") String customerId) {
    var command = DeleteCustomerCommand.builder()
        .customerId(customerId)
        .activeSw(CustomerConstants.IN_ACTIVE_SW)
        .build();

    commandGateway.sendAndWait(command);

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(new ResponseDto(CustomerConstants.STATUS_204, CustomerConstants.MESSAGE_204));
  }
}

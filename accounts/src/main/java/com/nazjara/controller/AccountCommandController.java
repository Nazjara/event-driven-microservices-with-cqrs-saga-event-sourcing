package com.nazjara.controller;

import static com.nazjara.constant.AccountsConstants.ACTIVE_SW;
import static com.nazjara.constant.AccountsConstants.IN_ACTIVE_SW;
import static com.nazjara.constant.AccountsConstants.MESSAGE_200;
import static com.nazjara.constant.AccountsConstants.MESSAGE_201;
import static com.nazjara.constant.AccountsConstants.MESSAGE_204;
import static com.nazjara.constant.AccountsConstants.STATUS_200;
import static com.nazjara.constant.AccountsConstants.STATUS_201;
import static com.nazjara.constant.AccountsConstants.STATUS_204;

import com.nazjara.command.CreateAccountCommand;
import com.nazjara.command.DeleteAccountCommand;
import com.nazjara.command.UpdateAccountCommand;
import com.nazjara.dto.AccountsDto;
import com.nazjara.dto.ResponseDto;
import jakarta.validation.Valid;
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
public class AccountCommandController {

  private final CommandGateway commandGateway;

  @PostMapping("/create")
  public ResponseEntity<ResponseDto> createAccount(@Valid @RequestBody AccountsDto accountsDto) {
    var command = CreateAccountCommand.builder()
        .accountNumber(accountsDto.getAccountNumber())
        .accountType(accountsDto.getAccountType())
        .branchAddress(accountsDto.getBranchAddress())
        .mobileNumber(accountsDto.getMobileNumber())
        .activeSw(ACTIVE_SW)
        .build();

    commandGateway.sendAndWait(command);

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(new ResponseDto(STATUS_201, MESSAGE_201));
  }

  @PutMapping("/update")
  public ResponseEntity<ResponseDto> updateAccountDetails(
      @Valid @RequestBody AccountsDto accountsDto) {
    var command = UpdateAccountCommand.builder()
        .accountNumber(accountsDto.getAccountNumber())
        .accountType(accountsDto.getAccountType())
        .branchAddress(accountsDto.getBranchAddress())
        .mobileNumber(accountsDto.getMobileNumber())
        .activeSw(ACTIVE_SW)
        .build();

    commandGateway.sendAndWait(command);

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(new ResponseDto(STATUS_200, MESSAGE_200));
  }

  @DeleteMapping("/delete")
  public ResponseEntity<ResponseDto> deleteAccount(
      @RequestParam("accountNumber") Long accountNumber) {
    var command = DeleteAccountCommand.builder()
        .accountNumber(accountNumber)
        .activeSw(IN_ACTIVE_SW)
        .build();

    commandGateway.sendAndWait(command);

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(new ResponseDto(STATUS_204, MESSAGE_204));
  }
}

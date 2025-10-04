package com.nazjara.mapper;

import com.nazjara.dto.AccountsDto;
import com.nazjara.entity.Accounts;
import com.nazjara.event.AccountUpdatedEvent;
import lombok.experimental.UtilityClass;

// can use Mapstruct instead
@UtilityClass
public class AccountsMapper {

  public AccountsDto mapToAccountsDto(Accounts accounts, AccountsDto accountsDto) {
    accountsDto.setAccountNumber(accounts.getAccountNumber());
    accountsDto.setAccountType(accounts.getAccountType());
    accountsDto.setBranchAddress(accounts.getBranchAddress());
    return accountsDto;
  }

  public Accounts mapToAccounts(AccountsDto accountsDto, Accounts accounts) {
    accounts.setAccountNumber(accountsDto.getAccountNumber());
    accounts.setAccountType(accountsDto.getAccountType());
    accounts.setBranchAddress(accountsDto.getBranchAddress());
    return accounts;
  }

  public Accounts mapEventToAccount(AccountUpdatedEvent event, Accounts accounts) {
    accounts.setAccountType(event.getAccountType());
    accounts.setBranchAddress(event.getBranchAddress());
    return accounts;
  }
}

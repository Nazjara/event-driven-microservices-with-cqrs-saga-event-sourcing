package com.nazjara.service;

import com.nazjara.constant.AccountsConstants;
import com.nazjara.dto.AccountsDto;
import com.nazjara.entity.Accounts;
import com.nazjara.event.AccountUpdatedEvent;
import com.nazjara.exception.AccountAlreadyExistsException;
import com.nazjara.exception.ResourceNotFoundException;
import com.nazjara.mapper.AccountsMapper;
import com.nazjara.repository.AccountsRepository;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Implementation of the IAccountsService interface that provides account-related operations
 */
@Service
@RequiredArgsConstructor
public class AccountsServiceImpl implements IAccountsService {

  private final AccountsRepository accountsRepository;

  /**
   * Creates a new account if no active account exists with the specified mobile number.
   *
   * @param accounts - An instance of {@code Accounts} containing account details such as mobile
   *                 number. This object is used to create a new account if no active account is
   *                 found.
   * @throws AccountAlreadyExistsException if an active account already exists using the given
   *                                       mobile number.
   */
  @Override
  public void createAccount(Accounts accounts) {
    var optionalAccounts = accountsRepository.findByMobileNumberAndActiveSw(
        accounts.getMobileNumber(),
        AccountsConstants.ACTIVE_SW);

    if (optionalAccounts.isPresent()) {
      throw new AccountAlreadyExistsException(
          "Account already registered with given mobileNumber " + accounts.getMobileNumber());
    }

    accountsRepository.save(createNewAccount(accounts.getMobileNumber()));
  }

  /**
   * @param mobileNumber - String
   * @return the new account details
   */
  private Accounts createNewAccount(String mobileNumber) {
    var newAccount = new Accounts();
    newAccount.setMobileNumber(mobileNumber);
    var randomAccNumber = 1000000000L + new Random().nextInt(900000000);
    newAccount.setAccountNumber(randomAccNumber);
    newAccount.setAccountType(AccountsConstants.SAVINGS);
    newAccount.setBranchAddress(AccountsConstants.ADDRESS);
    newAccount.setActiveSw(AccountsConstants.ACTIVE_SW);
    return newAccount;
  }

  /**
   * @param mobileNumber - Input Mobile Number
   * @return Accounts Details based on a given mobileNumber
   */
  @Override
  public AccountsDto fetchAccount(String mobileNumber) {
    var account = accountsRepository.findByMobileNumberAndActiveSw(mobileNumber,
            AccountsConstants.ACTIVE_SW)
        .orElseThrow(() -> new ResourceNotFoundException("Account", "mobileNumber", mobileNumber)
        );

    var accountsDto = AccountsMapper.mapToAccountsDto(account, new AccountsDto());
    return accountsDto;
  }

  /**
   * Updates an account using the details provided in the {@code AccountUpdatedEvent}. This method
   * retrieves the account based on the mobile number and active status, updates its details, and
   * persists the changes to the database.
   *
   * @param event - An instance of {@code AccountUpdatedEvent} containing the updated account
   *              details, including mobile number and other optional parameters.
   * @return {@code true} if the account update operation is successful.
   * @throws ResourceNotFoundException if no active account is found for the specified mobile
   *                                   number.
   */
  @Override
  public boolean updateAccount(AccountUpdatedEvent event) {
    var account = accountsRepository.findByMobileNumberAndActiveSw(
            event.getMobileNumber(),
            AccountsConstants.ACTIVE_SW)
        .orElseThrow(() -> new ResourceNotFoundException("Account", "mobileNumber",
            event.getMobileNumber()));

    accountsRepository.save(account);
    return true;
  }

  /**
   * @param accountNumber - Input Account Number
   * @return boolean indicating if the delete of Account details is successful or not
   */
  @Override
  public boolean deleteAccount(Long accountNumber) {
    var account = accountsRepository.findById(accountNumber).orElseThrow(
        () -> new ResourceNotFoundException("Account", "accountNumber", accountNumber.toString())
    );
    account.setActiveSw(AccountsConstants.IN_ACTIVE_SW);
    accountsRepository.save(account);
    return true;
  }

  @Override
  public boolean updateMobileNumber(String oldMobileNumber, String newMobileNumber) {
    var account = accountsRepository.findByMobileNumberAndActiveSw(oldMobileNumber, true)
        .orElseThrow(
            () -> new ResourceNotFoundException("Account", "mobileNumber", oldMobileNumber)
        );

    account.setMobileNumber(newMobileNumber);
    accountsRepository.save(account);
    return true;
  }
}

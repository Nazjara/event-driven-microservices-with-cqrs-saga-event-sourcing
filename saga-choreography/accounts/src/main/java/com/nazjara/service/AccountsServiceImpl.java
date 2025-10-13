package com.nazjara.service;

import com.nazjara.constant.AccountsConstants;
import com.nazjara.dto.AccountsDto;
import com.nazjara.dto.MobileNumberUpdateDto;
import com.nazjara.entity.Accounts;
import com.nazjara.exception.AccountAlreadyExistsException;
import com.nazjara.exception.ResourceNotFoundException;
import com.nazjara.mapper.AccountsMapper;
import com.nazjara.repository.AccountsRepository;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of the IAccountsService interface that provides account-related operations
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AccountsServiceImpl implements IAccountsService {

  private final AccountsRepository accountsRepository;
  private final StreamBridge streamBridge;

  /**
   * @param mobileNumber - String
   */
  @Override
  public void createAccount(String mobileNumber) {
    var optionalAccounts = accountsRepository.findByMobileNumberAndActiveSw(
        mobileNumber,
        AccountsConstants.ACTIVE_SW);

    if (optionalAccounts.isPresent()) {
      throw new AccountAlreadyExistsException(
          "Account already registered with given mobileNumber " + mobileNumber);
    }

    accountsRepository.save(createNewAccount(mobileNumber));
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
   * @param accountsDto - AccountsDto Object
   * @return boolean indicating if the update of Account details is successful or not
   */
  @Override
  public boolean updateAccount(AccountsDto accountsDto) {
    var account = accountsRepository.findByMobileNumberAndActiveSw(
            accountsDto.getMobileNumber(),
            AccountsConstants.ACTIVE_SW)
        .orElseThrow(() -> new ResourceNotFoundException("Account", "mobileNumber",
            accountsDto.getMobileNumber()));

    AccountsMapper.mapToAccounts(accountsDto, account);
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

  /**
   * Updates the mobile number associated with an account.
   *
   * @param mobileNumberUpdateDto an object containing the current mobile number and the new mobile
   *                              number to be updated
   */
  @Override
  @Transactional
  public void updateMobileNumber(MobileNumberUpdateDto mobileNumberUpdateDto) {
    var mobileNumber = mobileNumberUpdateDto.getCurrentMobileNumber();
    var accounts = accountsRepository.findByMobileNumberAndActiveSw(mobileNumber, true)
        .orElseThrow(
            () -> new ResourceNotFoundException("Accounts", "mobileNumber", mobileNumber)
        );

    accounts.setMobileNumber(mobileNumberUpdateDto.getNewMobileNumber());
    updateCardMobileNumber(mobileNumberUpdateDto);
  }

  @Override
  @Transactional
  public void rollbackMobileNumberUpdate(MobileNumberUpdateDto mobileNumberUpdateDto) {
    var mobileNumber = mobileNumberUpdateDto.getNewMobileNumber();
    var accounts = accountsRepository.findByMobileNumberAndActiveSw(mobileNumber, true)
        .orElseThrow(
            () -> new ResourceNotFoundException("Account", "mobileNumber", mobileNumber)
        );

    accounts.setMobileNumber(mobileNumberUpdateDto.getCurrentMobileNumber());
  }

  private void updateCardMobileNumber(MobileNumberUpdateDto mobileNumberUpdateDto) {
    log.info("Sending message to update card mobile number. Details: {}", mobileNumberUpdateDto);
    var result = streamBridge.send("updateCardMobileNumber-out-0", mobileNumberUpdateDto);
    if (!result) {
      log.error("Failed to send card mobile number update message");
      // need to throw an exception here to roll back the local transaction
      // TODO: use custom exception to handle this scenario
      throw new RuntimeException("Failed to send message to card service");
    }
    log.info("Message sent to update card mobile number");
  }
}

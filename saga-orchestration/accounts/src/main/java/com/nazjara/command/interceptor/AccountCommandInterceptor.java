package com.nazjara.command.interceptor;

import com.nazjara.command.CreateAccountCommand;
import com.nazjara.command.DeleteAccountCommand;
import com.nazjara.command.UpdateAccountCommand;
import com.nazjara.exception.AccountAlreadyExistsException;
import com.nazjara.exception.ResourceNotFoundException;
import com.nazjara.repository.AccountsRepository;
import java.util.List;
import java.util.function.BiFunction;
import javax.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountCommandInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {

  private final AccountsRepository repository;

  @Nonnull
  @Override
  public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(
      @Nonnull List<? extends CommandMessage<?>> messages) {
    return (index, command) -> {
      switch (command.getPayload()) {
        case CreateAccountCommand createAccountCommand -> {
          var account = repository.findByMobileNumberAndActiveSw(
              createAccountCommand.getMobileNumber(), true);

          if (account.isPresent()) {
            throw new AccountAlreadyExistsException(
                String.format("Account already registered with given mobile number: %s",
                    createAccountCommand.getMobileNumber()));
          }
        }

        case UpdateAccountCommand updateAccountCommand -> repository.findByMobileNumberAndActiveSw(
            updateAccountCommand.getMobileNumber(), true).orElseThrow(() ->
            new ResourceNotFoundException("Account", "mobileNumber",
                updateAccountCommand.getMobileNumber()));

        case DeleteAccountCommand deleteAccountCommand -> repository.findByAccountNumberAndActiveSw(
            deleteAccountCommand.getAccountNumber(), true).orElseThrow(() ->
            new ResourceNotFoundException("Account", "accountNumber",
                deleteAccountCommand.getAccountNumber().toString()));

        default -> throw new IllegalStateException("Unexpected value: " + command.getPayload());
      }

      return command;
    };
  }
}

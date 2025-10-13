package com.nazjara.command.interceptor;

import com.nazjara.command.CreateCustomerCommand;
import com.nazjara.command.DeleteCustomerCommand;
import com.nazjara.command.UpdateCustomerCommand;
import com.nazjara.exception.CustomerAlreadyExistsException;
import com.nazjara.exception.ResourceNotFoundException;
import com.nazjara.repository.CustomerRepository;
import java.util.List;
import java.util.function.BiFunction;
import javax.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerCommandInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {

  private final CustomerRepository repository;

  @Nonnull
  @Override
  public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(
      @Nonnull List<? extends CommandMessage<?>> messages) {
    return (index, command) -> {
      switch (command.getPayload()) {
        case CreateCustomerCommand createCustomerCommand -> {
          var customer = repository.findByMobileNumberAndActiveSw(
              createCustomerCommand.getMobileNumber(), true);

          if (customer.isPresent()) {
            throw new CustomerAlreadyExistsException(
                String.format("Customer already registered with given mobile number: %s",
                    createCustomerCommand.getMobileNumber()));
          }
        }

        case UpdateCustomerCommand updateCustomerCommand ->
            repository.findByMobileNumberAndActiveSw(
                updateCustomerCommand.getMobileNumber(), true).orElseThrow(() ->
                new ResourceNotFoundException("Customer", "mobileNumber",
                    updateCustomerCommand.getMobileNumber()));

        case DeleteCustomerCommand deleteCustomerCommand -> repository.findByCustomerIdAndActiveSw(
            deleteCustomerCommand.getCustomerId(), true).orElseThrow(() ->
            new ResourceNotFoundException("Customer", "mobileNumber",
                deleteCustomerCommand.getCustomerId()));

        default -> throw new IllegalStateException("Unexpected value: " + command.getPayload());
      }

      return command;
    };
  }
}

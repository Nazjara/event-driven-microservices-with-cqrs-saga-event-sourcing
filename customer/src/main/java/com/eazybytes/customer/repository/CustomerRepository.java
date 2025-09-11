package com.eazybytes.customer.repository;

import com.eazybytes.customer.entity.Customer;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {

  Optional<Customer> findByMobileNumberAndActiveSw(String mobileNumber, boolean active);
}

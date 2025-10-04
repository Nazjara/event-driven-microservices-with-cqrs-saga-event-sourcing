package com.nazjara.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Customer extends BaseEntity {

  @Id
  @Column(name = "customer_id", length = 100)
  private String customerId;

  @Column(name = "name", length = 100, nullable = false)
  private String name;

  @Column(name = "email", length = 100, nullable = false)
  private String email;

  @Column(name = "mobile_number", length = 20, nullable = false)
  private String mobileNumber;

  @Column(name = "active_sw", nullable = false)
  private boolean activeSw = false;
}

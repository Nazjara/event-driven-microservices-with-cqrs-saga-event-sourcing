package com.nazjara.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
public class Profile extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "profile_id", unique = true)
  private long profileId;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "mobile_number", nullable = false, unique = true)
  private String mobileNumber;

  @Column(name = "active_sw", nullable = false)
  private boolean activeSw = false;

  @Column(name = "account_number")
  private String accountNumber;

  @Column(name = "card_number")
  private boolean cardNumber;

  @Column(name = "loan_number")
  private long loanNumber;
}

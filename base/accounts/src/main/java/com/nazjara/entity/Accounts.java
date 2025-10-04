package com.nazjara.entity;

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
public class Accounts extends BaseEntity {

  @Id
  private Long accountNumber;
  private String accountType;
  private String branchAddress;
  private String mobileNumber;
  private boolean activeSw;
}

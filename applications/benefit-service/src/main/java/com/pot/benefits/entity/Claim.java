package com.pot.benefits.entity;

import com.pot.database.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Claim extends BaseEntity {

  private String serialNumber;

  @ManyToOne
  @JoinColumn(name = "enrolment_id")
  private Enrolment enrolment;

  private BigDecimal amount;
  private LocalDate serviceDate;

  @Embedded private ClaimInformation claimInformation;
}

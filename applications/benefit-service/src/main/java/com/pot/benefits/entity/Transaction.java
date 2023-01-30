package com.pot.benefits.entity;

import com.pot.common.enums.TransactionType;
import com.pot.database.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
public class Transaction extends BaseEntity {
  @ManyToOne
  @JoinColumn(name = "enrolment_id")
  private Enrolment enrolment;

  @ManyToOne
  @JoinColumn(name = "claim_id")
  private Claim claim;

  private BigDecimal amount;

  @Enumerated(value = EnumType.STRING)
  private TransactionType type;
}

package com.pot.benefits.entity;

import com.pot.common.enums.EnrolmentStatus;
import com.pot.common.enums.PayrollFrequency;
import com.pot.database.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Enrolment extends BaseEntity {
  private String userProfileId;

  @ManyToOne
  @JoinColumn(name = "insurance_id")
  private Insurance insurance;

  private BigDecimal election;
  private BigDecimal contribution;

  @Enumerated(value = EnumType.STRING)
  private PayrollFrequency payrollFrequency;

  private LocalDate lastProcessedDate;
  private LocalDate dueDate;

  @Enumerated(value = EnumType.STRING)
  private EnrolmentStatus status;
}

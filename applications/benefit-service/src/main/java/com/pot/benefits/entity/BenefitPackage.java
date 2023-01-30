package com.pot.benefits.entity;

import com.pot.common.enums.PackageStatus;
import com.pot.common.enums.PayrollFrequency;
import com.pot.database.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class BenefitPackage extends BaseEntity {
  private String name;
  private String companyId;

  @Enumerated(value = EnumType.STRING)
  private PayrollFrequency payrollFrequency;

  private LocalDate startDate;
  private LocalDate endDate;

  @Enumerated(value = EnumType.STRING)
  private PackageStatus status;
}

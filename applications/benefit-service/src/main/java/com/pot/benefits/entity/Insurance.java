package com.pot.benefits.entity;

import com.pot.common.enums.InsuranceType;
import com.pot.database.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
public class Insurance extends BaseEntity {
  private String name;

  @Enumerated(value = EnumType.STRING)
  private InsuranceType type;

  private BigDecimal contribution;

  @ManyToOne
  @JoinColumn(name = "benefit_package_id")
  private BenefitPackage benefitPackage;
}

package com.pot.benefits.dto;

import com.pot.common.enums.InsuranceType;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.math.BigDecimal;

@Data
public class EnrolmentDto {
  @Null private String id;
  @NotNull private String userProfileId;
  @NotNull private String insuranceId;
  @NotNull private BigDecimal election;
  @Null private String insuranceName;
  @Null private InsuranceType insuranceType;
  @Null private BigDecimal contribution;
}

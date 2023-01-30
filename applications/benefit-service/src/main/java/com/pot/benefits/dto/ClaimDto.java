package com.pot.benefits.dto;

import com.pot.common.enums.ClaimStatus;
import com.pot.common.enums.InsuranceType;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ClaimDto {
  @Null private String id;
  @NotNull private String serialNumber;
  @NotNull private BigDecimal amount;
  @NotNull private LocalDate serviceDate;
  @NotNull private String enrolmentId;
  @Null private InsuranceType insuranceType;
  @Null private ClaimStatus status;
}

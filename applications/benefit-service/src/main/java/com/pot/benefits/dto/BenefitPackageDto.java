package com.pot.benefits.dto;

import com.pot.benefits.service.validators.StartDateBeforeEndDate;
import com.pot.common.enums.PackageStatus;
import com.pot.common.enums.PayrollFrequency;
import com.pot.common.validations.OnCreate;
import com.pot.common.validations.OnUpdate;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@StartDateBeforeEndDate
public class BenefitPackageDto {
  @Null(groups = OnCreate.class)
  @NotNull(groups = OnUpdate.class)
  private String id;

  @NotEmpty private String name;
  @NotEmpty private String companyId;
  @NotNull private PayrollFrequency payrollFrequency;
  @NotNull @FutureOrPresent private LocalDate startDate;
  @NotNull @Future private LocalDate endDate;
  @Null private PackageStatus status;
}

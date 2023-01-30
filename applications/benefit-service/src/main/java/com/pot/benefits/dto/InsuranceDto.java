package com.pot.benefits.dto;

import com.pot.common.enums.InsuranceType;
import com.pot.common.validations.OnCreate;
import com.pot.common.validations.OnUpdate;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.math.BigDecimal;

@Data
public class InsuranceDto {
  @Null(groups = OnCreate.class)
  @NotNull(groups = OnUpdate.class)
  private String id;

  @NotEmpty private String name;
  @NotNull private InsuranceType type;
  @NotNull private BigDecimal contribution;
}

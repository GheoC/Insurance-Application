package com.pot.benefits.dto;

import com.pot.common.enums.ClaimStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClaimInformationDto {
  @NotNull private ClaimStatus status;

  @NotEmpty
  @Max(value = 500)
  private String comment;
}

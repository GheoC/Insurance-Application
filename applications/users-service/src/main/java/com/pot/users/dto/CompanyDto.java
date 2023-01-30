package com.pot.users.dto;

import com.pot.common.enums.CompanyStatus;
import com.pot.common.validations.OnCreate;
import com.pot.common.validations.OnUpdate;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.*;

@Data
public class CompanyDto {

  @Null(groups = OnCreate.class)
  @NotNull(groups = OnUpdate.class)
  private String id;

  @NotEmpty
  @Size(max = 80)
  private String companyName;

  @NotEmpty
  @Size(max = 3)
  private String countryCode;

  @Email
  @Size(max = 80)
  private String email;

  @NotEmpty
  @Size(max = 150)
  private String site;

  @Null private CompanyStatus status;

  @Valid @NotNull private ContactInfoDto contactInfo;
}

package com.pot.users.dto;

import com.pot.common.enums.FunctionName;
import com.pot.common.validations.OnCreate;
import com.pot.common.validations.OnUpdate;
import com.pot.error.validators.NotContainsSuperAdmin;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
public class UserProfileDto {
  @Null(groups = OnCreate.class)
  @NotNull(groups = OnUpdate.class)
  private String id;

  @Valid
  @NotNull(groups = OnCreate.class)
  @Null(groups = OnUpdate.class)
  private UserDto user;

  @Valid @NotNull private UserDetailsDto userDetails;

  @Valid @NotNull private ContactInfoDto contactInfo;

  @Size(min = 1)
  @NotContainsSuperAdmin
  private List<FunctionName> roles = new ArrayList<>();
}

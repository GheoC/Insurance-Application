package com.pot.users.dto;

import com.pot.common.validations.OnCreate;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

@Data
public class UserDto {
  @Null(groups = OnCreate.class)
  private String id;

  @NotEmpty
  @Size(max = 60)
  private String username;

  @NotEmpty
  @Size(max = 80)
  private String password;
}

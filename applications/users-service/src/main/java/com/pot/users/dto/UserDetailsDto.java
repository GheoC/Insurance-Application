package com.pot.users.dto;

import com.pot.common.validations.OnCreate;
import com.pot.common.validations.OnUpdate;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class UserDetailsDto {
  @Null(groups = OnCreate.class)
  @NotNull(groups = OnUpdate.class)
  private String id;

  @NotEmpty
  @Size(max = 80)
  private String firstName;

  @NotEmpty
  @Size(max = 80)
  private String lastName;

  @NotEmpty
  @Size(max = 80)
  private String email;

  @NotEmpty
  @Size(max = 20)
  private String snn;

  @NotNull
  @Past
  private LocalDate birthday;
}

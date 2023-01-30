package com.pot.users.dto;

import com.pot.common.validations.OnCreate;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class PhoneDto {
  @Null(groups = OnCreate.class)
  private String id;

  @NotEmpty
  @Size(max = 3)
  @Pattern(regexp = "^[a-zA-Z]*$")
  private String countryCode;

  @NotEmpty
  @Size(max = 11)
  @Pattern(regexp = "[0-9]+")
  private String phoneNumber;
}

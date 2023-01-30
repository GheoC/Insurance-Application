package com.pot.users.dto;

import com.pot.common.validations.OnCreate;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

@Data
public class AddressDto {
  @Null(groups = OnCreate.class)
  private String id;

  @NotEmpty
  @Size(max = 50)
  private String country;

  @NotEmpty
  @Size(max = 50)
  private String city;

  @NotEmpty
  @Size(max = 50)
  private String street;

  @NotEmpty
  @Size(max = 10)
  private String building;

  @NotEmpty
  @Size(max = 50)
  private String state;

  @NotEmpty
  @Size(max = 5)
  private String room;
}

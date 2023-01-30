package com.pot.users.dto;

import com.pot.common.validations.OnCreate;
import com.pot.common.validations.OnUpdate;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
public class ContactInfoDto {
  @Null(groups = OnCreate.class)
  @NotNull(groups = OnUpdate.class)
  private String id;

  @Size(min = 1)
  List<@Valid @NotNull AddressDto> addresses = new ArrayList<>();

  @Size(min = 1)
  List<@Valid @NotNull PhoneDto> phones = new ArrayList<>();
}

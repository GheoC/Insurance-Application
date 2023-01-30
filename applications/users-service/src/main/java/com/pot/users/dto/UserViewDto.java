package com.pot.users.dto;

import com.pot.common.enums.FunctionName;
import com.pot.common.enums.UserStatus;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class UserViewDto {
  private String id;
  private String companyId;
  private String firstName;
  private String lastName;
  private String email;
  private LocalDate birthday;
  private UserStatus status;
  private String snn;
  private List<FunctionName> roles;
}

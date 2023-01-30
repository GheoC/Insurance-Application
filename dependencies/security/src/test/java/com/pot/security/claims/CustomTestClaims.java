package com.pot.security.claims;

import com.pot.common.enums.FunctionName;
import com.pot.common.enums.UserStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public abstract class CustomTestClaims {
  @JsonProperty("user_name")
  private String username;

  private String userId;
  private UserStatus status;
  private List<FunctionName> authorities = new ArrayList<>();

  public CustomTestClaims(
      String username, String userId, UserStatus status, List<FunctionName> roles) {
    this.username = username;
    this.userId = userId;
    this.status = status;
    this.authorities.addAll(roles);
  }
}

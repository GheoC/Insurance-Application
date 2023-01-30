package com.pot.security.claims;

import com.pot.common.enums.FunctionName;
import com.pot.common.enums.UserStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Getter
public class AuthUserHelper {

  public AdminClaims createDefaultAdminClaims() {
    return new AdminClaims("george", "11111", UserStatus.ACTIVE, List.of(FunctionName.SUPER_ADMIN));
  }

  public UserClaims createDefaultUserClaims() {
    return new UserClaims(
        "george",
        "11111",
        UserStatus.ACTIVE,
        List.of(FunctionName.CONSUMER),
        "gg@gmail.com",
        "222222",
        "33333");
  }

  public AdminClaims createCustomAdminClaims(
      String username, String userId, UserStatus status, List<FunctionName> roles) {
    return new AdminClaims(username, userId, status, roles);
  }

  public UserClaims createCustomUserClaims(
      String username,
      String userId,
      UserStatus status,
      List<FunctionName> roles,
      String userEmail,
      String userProfileId,
      String companyId) {
    return new UserClaims(username, userId, status, roles, userEmail, userProfileId, companyId);
  }
}

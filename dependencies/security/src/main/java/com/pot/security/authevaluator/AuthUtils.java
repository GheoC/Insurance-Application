package com.pot.security.authevaluator;

import com.pot.common.enums.FunctionName;
import com.pot.security.entity.AuthUser;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.stream.Collectors;

public class AuthUtils {
  public static String getUserCompanyId() {
    return getAuthUser().getCompanyId();
  }

  public static List<FunctionName> getUserRoles() {
    return getAuthUser().getAuthorities().stream()
        .map(it -> Enum.valueOf(FunctionName.class, it.getAuthority().replace("ROLE_", "")))
        .collect(Collectors.toList());
  }

  public static AuthUser getAuthUser() {
    return (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }
}

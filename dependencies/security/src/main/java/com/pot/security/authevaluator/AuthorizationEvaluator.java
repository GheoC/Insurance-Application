package com.pot.security.authevaluator;

import com.pot.common.enums.FunctionName;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationEvaluator {

  public boolean isAdminOrCompanyManager(String companyId) {
    var roles = AuthUtils.getUserRoles();
    boolean isAdmin = roles.contains(FunctionName.SUPER_ADMIN);
    boolean isCompanyManager =
        roles.contains(FunctionName.COMPANY_MANAGER)
            && companyId.equals(AuthUtils.getUserCompanyId());

    return (isAdmin || isCompanyManager);
  }

  public boolean isAccountOwner(String id) {
    return id.equals(AuthUtils.getAuthUser().getUserProfileId());
  }

  public boolean isCompanySettingManager(String companyId) {
    return (AuthUtils.getUserRoles().contains(FunctionName.COMPANY_SETTING_MANAGER)
        && companyId.equals(AuthUtils.getUserCompanyId()));
  }
}

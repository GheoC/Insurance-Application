package com.pot.users;

import com.pot.security.BaseSecurityTestClass;
import com.pot.security.claims.AuthUserHelper;
import com.pot.users.helpers.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@RequiredArgsConstructor
public class BaseUsersServiceTestClass extends BaseSecurityTestClass {
  @Autowired protected AddressHelper addressHelper;
  @Autowired protected PhoneHelper phoneHelper;
  @Autowired protected ContactInfoHelper contactInfoHelper;
  @Autowired protected CompanyHelper companyHelper;
  @Autowired protected FunctionHelper functionHelper;
  @Autowired protected AuthUserHelper authUserHelper;
  @Autowired protected UserDetailsHelper userDetailsHelper;
  @Autowired protected UserHelper userHelper;
  @Autowired protected ClientRoleHelper clientRoleHelper;
  @Autowired protected UserProfileHelper userProfileHelper;
  protected String url;
}

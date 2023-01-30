package com.pot.benefits;

import com.pot.benefits.helpers.*;
import com.pot.security.BaseSecurityTestClass;
import com.pot.security.claims.AuthUserHelper;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseBenefitServiceTestClass extends BaseSecurityTestClass {
  @Autowired protected BenefitPackageHelper benefitPackageHelper;
  @Autowired protected AuthUserHelper authUserHelper;
  @Autowired protected InsuranceHelper insuranceHelper;
  @Autowired protected EnrolmentHelper enrolmentHelper;
  @Autowired protected ClaimHelper claimHelper;
  @Autowired protected TransactionHelper transactionHelper;
}

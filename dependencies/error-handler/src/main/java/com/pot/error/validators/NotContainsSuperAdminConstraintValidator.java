package com.pot.error.validators;

import com.pot.common.enums.FunctionName;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class NotContainsSuperAdminConstraintValidator
    implements ConstraintValidator<NotContainsSuperAdmin, List<FunctionName>> {
  @Override
  public boolean isValid(List<FunctionName> values, ConstraintValidatorContext context) {
    return !values.contains(FunctionName.SUPER_ADMIN);
  }
}

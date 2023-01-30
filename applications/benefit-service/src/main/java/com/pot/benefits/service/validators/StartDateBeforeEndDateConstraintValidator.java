package com.pot.benefits.service.validators;

import com.pot.benefits.dto.BenefitPackageDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class StartDateBeforeEndDateConstraintValidator
    implements ConstraintValidator<StartDateBeforeEndDate, BenefitPackageDto> {
  @Override
  public boolean isValid(BenefitPackageDto value, ConstraintValidatorContext context) {
    if (value.getStartDate() == null || value.getEndDate() == null) {
      return false;
    }
    return value.getStartDate().isBefore(value.getEndDate());
  }
}

package com.pot.error.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Constraint(validatedBy = NotContainsSuperAdminConstraintValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface NotContainsSuperAdmin {
  String message() default "Must not contain SUPER_ADMIN";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}

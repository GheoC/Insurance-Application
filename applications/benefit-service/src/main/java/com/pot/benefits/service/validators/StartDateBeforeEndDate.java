package com.pot.benefits.service.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Constraint(validatedBy = StartDateBeforeEndDateConstraintValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface StartDateBeforeEndDate {
    String message() default "Ending date must be at least 1 day over Starting date";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

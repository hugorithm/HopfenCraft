package com.hugorithm.hopfencraft.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PositiveBigDecimalValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface PositiveBigDecimal {
    String message() default "Value must be positive";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}


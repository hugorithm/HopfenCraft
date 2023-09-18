package com.hugorithm.hopfencraft.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PhoneValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Phone {
    String message() default "Invalid mobile phone format";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

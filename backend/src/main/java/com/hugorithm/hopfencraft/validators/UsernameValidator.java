package com.hugorithm.hopfencraft.validators;

import com.hugorithm.hopfencraft.exception.auth.UsernameNotValidException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UsernameValidator implements ConstraintValidator<Username, String> {
    private static final String USERNAME_REGEX = "^(?!.*[-_.]{2})[a-zA-Z0-9-_.]{3,20}$";
    private static final Pattern pattern = Pattern.compile(USERNAME_REGEX);

    @Override
    public void initialize(Username constraintAnnotation) {
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        Matcher matcher = pattern.matcher(username);
        if (matcher.matches()) {
            return true;
        } else {
            throw new UsernameNotValidException("Invalid username");
        }
    }
}

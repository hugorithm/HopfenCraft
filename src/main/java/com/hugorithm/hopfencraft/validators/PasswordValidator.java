package com.hugorithm.hopfencraft.validators;

import com.hugorithm.hopfencraft.exception.PasswordNotValidException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password, String> {

    @Override
    public void initialize(Password constraintAnnotation) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {

        if (password == null || password.isEmpty()) {
            throw new PasswordNotValidException("Password is Null or Empty");
        }

        if (password.length() < 8) {
            throw new PasswordNotValidException("Password must have at least 8 characters");
        }

        if (!password.matches(".*[A-Z].*")) {
            throw new PasswordNotValidException("Password must contain at least 1 Uppercase character");
        }

        if (!password.matches(".*[a-z].*")) {
            throw new PasswordNotValidException("Password must contain at least 1 lowerCase character");
        }

        if (!password.matches(".*\\d.*")) {
            throw new PasswordNotValidException("Password must contain at least 1 digit");
        }

        if (!password.matches(".*[^\\s\\p{C}].*")) {
            throw new PasswordNotValidException("Password must not contain whitespace or control characters");
        }

        if (!password.matches(".*[!@#$%^&*()_+{}\\[\\]:;<>,.?~\\\\|\\-].*")) {
            throw new PasswordNotValidException("Password must  contain at least 1 special character");
        }

        return true;
    }
}

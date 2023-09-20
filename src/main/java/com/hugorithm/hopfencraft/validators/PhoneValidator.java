package com.hugorithm.hopfencraft.validators;

import com.hugorithm.hopfencraft.exception.user.PhoneNumberNotValidException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PhoneValidator implements ConstraintValidator<Phone, String> {

    private static final String PHONE_REGEX = "^\\+?[0-9]{10,15}$";
    private static final Pattern pattern = Pattern.compile(PHONE_REGEX);

    @Override
    public void initialize(Phone constraintAnnotation) {
    }

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        Matcher matcher = pattern.matcher(phoneNumber);
        if (matcher.matches()) {
            return true;
        } else {
            throw new PhoneNumberNotValidException("Invalid phone number");
        }
    }
}
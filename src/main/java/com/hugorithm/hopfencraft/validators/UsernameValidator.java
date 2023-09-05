package com.hugorithm.hopfencraft.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UsernameValidator {
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_-]{3,20}$";
    private static final Pattern pattern = Pattern.compile(EMAIL_REGEX);
    public static boolean isUsernameValid(String username){
        Matcher matcher = pattern.matcher(username);
        return matcher.matches();
    }
}

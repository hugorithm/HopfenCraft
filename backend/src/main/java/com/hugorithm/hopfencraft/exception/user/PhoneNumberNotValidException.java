package com.hugorithm.hopfencraft.exception.user;

public class PhoneNumberNotValidException extends RuntimeException{
    public PhoneNumberNotValidException(String message, Object... args) {
        super(String.format(message, args));
    }
}

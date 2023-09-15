package com.hugorithm.hopfencraft.exception.auth;

public class WrongCredentialsException extends RuntimeException {
    public WrongCredentialsException(String message, Object... args) {
        super(String.format(message, args));
    }
}

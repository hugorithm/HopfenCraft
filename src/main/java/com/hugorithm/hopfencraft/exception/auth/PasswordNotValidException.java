package com.hugorithm.hopfencraft.exception.auth;

public class PasswordNotValidException extends RuntimeException {
    public PasswordNotValidException(String message) {
        super(message);
    }
}
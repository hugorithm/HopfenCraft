package com.hugorithm.hopfencraft.exception.auth;

public class PasswordMismatchException extends RuntimeException{
    public PasswordMismatchException(String message) {
        super(message);
    }
}

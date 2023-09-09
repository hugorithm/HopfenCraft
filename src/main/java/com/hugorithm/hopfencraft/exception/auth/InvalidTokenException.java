package com.hugorithm.hopfencraft.exception.auth;

public class InvalidTokenException extends RuntimeException{
    public InvalidTokenException(String message) {
        super(message);
    }
}

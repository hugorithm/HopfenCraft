package com.hugorithm.hopfencraft.exception.email;

public class EmailAlreadyTakenException extends RuntimeException{
    public EmailAlreadyTakenException(String message, Object... args) {
        super(String.format(message, args));
    }
}

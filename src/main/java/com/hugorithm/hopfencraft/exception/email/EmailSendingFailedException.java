package com.hugorithm.hopfencraft.exception.email;

public class EmailSendingFailedException extends RuntimeException{
    public EmailSendingFailedException(String message, Object... args) {
        super(String.format(message, args));
    }
}

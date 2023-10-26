package com.hugorithm.hopfencraft.exception.email;

public class EmailSendingFailedException extends RuntimeException {
    public EmailSendingFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}

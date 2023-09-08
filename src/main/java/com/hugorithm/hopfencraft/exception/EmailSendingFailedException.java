package com.hugorithm.hopfencraft.exception;

public class EmailSendingFailedException extends RuntimeException{
    public EmailSendingFailedException(String message) {
        super(message);
    }
}

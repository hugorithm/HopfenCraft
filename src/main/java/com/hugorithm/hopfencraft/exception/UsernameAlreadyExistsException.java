package com.hugorithm.hopfencraft.exception;

public class UsernameAlreadyExistsException extends  RuntimeException{
    public UsernameAlreadyExistsException(String message) {
        super(message);
    }
}

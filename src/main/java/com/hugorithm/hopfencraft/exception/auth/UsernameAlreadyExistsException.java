package com.hugorithm.hopfencraft.exception.auth;

public class UsernameAlreadyExistsException extends  RuntimeException{
    public UsernameAlreadyExistsException(String message) {
        super(message);
    }
}

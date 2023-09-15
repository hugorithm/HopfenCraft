package com.hugorithm.hopfencraft.exception.auth;

public class UsernameNotValidException  extends RuntimeException  {
    public UsernameNotValidException(String message, Object... args) {
        super(String.format(message, args));
    }
}

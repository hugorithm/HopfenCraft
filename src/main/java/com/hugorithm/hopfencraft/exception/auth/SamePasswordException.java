package com.hugorithm.hopfencraft.exception.auth;

public class SamePasswordException extends RuntimeException{
    public SamePasswordException(String message) {
        super(message);
    }
}

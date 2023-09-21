package com.hugorithm.hopfencraft.exception.auth;

public class RsaKeyGenerationException extends RuntimeException{
    public RsaKeyGenerationException(String message, Object... args) {
        super(String.format(message, args));
    }
}

package com.hugorithm.hopfencraft.exception.paypal;

public class PaypalAccessTokenException extends RuntimeException{
    public PaypalAccessTokenException(String message, Object... args) {
        super(String.format(message, args));
    }
}

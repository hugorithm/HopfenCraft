package com.hugorithm.hopfencraft.exception.order;

public class OrderPaymentException extends RuntimeException{
    public OrderPaymentException(String message, Object... args) {
        super(String.format(message, args));
    }
}

package com.hugorithm.hopfencraft.exception.order;

public class OrderNotFoundException extends RuntimeException{
    public OrderNotFoundException(String message, Object... args) {
        super(String.format(message, args));
    }
}

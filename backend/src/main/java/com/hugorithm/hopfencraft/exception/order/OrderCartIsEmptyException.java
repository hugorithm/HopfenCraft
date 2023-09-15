package com.hugorithm.hopfencraft.exception.order;

public class OrderCartIsEmptyException extends RuntimeException{
    public OrderCartIsEmptyException(String message, Object... args) {
        super(String.format(message, args));
    }
}

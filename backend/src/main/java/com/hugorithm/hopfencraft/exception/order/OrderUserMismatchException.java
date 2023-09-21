package com.hugorithm.hopfencraft.exception.order;

public class OrderUserMismatchException extends RuntimeException{
    public OrderUserMismatchException(String message, Object... args) {
        super(String.format(message, args));
    }
}

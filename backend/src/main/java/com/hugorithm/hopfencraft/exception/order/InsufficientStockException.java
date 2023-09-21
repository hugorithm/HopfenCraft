package com.hugorithm.hopfencraft.exception.order;

public class InsufficientStockException extends RuntimeException{
    public InsufficientStockException(String message, Object... args) {
        super(String.format(message, args));
    }
}

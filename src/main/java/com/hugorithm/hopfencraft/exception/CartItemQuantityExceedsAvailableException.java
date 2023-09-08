package com.hugorithm.hopfencraft.exception;

public class CartItemQuantityExceedsAvailableException extends RuntimeException{
    public CartItemQuantityExceedsAvailableException(String message) {
        super(message);
    }
}

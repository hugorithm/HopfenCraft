package com.hugorithm.hopfencraft.exception.cart;

public class CartItemQuantityExceedsAvailableException extends RuntimeException{
    public CartItemQuantityExceedsAvailableException(String message, Object... args) {
        super(String.format(message, args));
    }
}

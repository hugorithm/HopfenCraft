package com.hugorithm.hopfencraft.exception.cart;

public class CartItemNotFoundException extends RuntimeException{
    public CartItemNotFoundException(String message, Object... args) {
        super(String.format(message, args));
    }
}

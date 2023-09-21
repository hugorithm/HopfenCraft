package com.hugorithm.hopfencraft.exception.product;

public class ProductNotFoundException extends RuntimeException{
    public ProductNotFoundException(String message, Object... args) {
        super(String.format(message, args));
    }
}

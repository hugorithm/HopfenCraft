package com.hugorithm.hopfencraft.exception.product;

public class ProductImageNotFoundException extends RuntimeException{
    public ProductImageNotFoundException(String message, Object... args) {
        super(String.format(message, args));
    }
}

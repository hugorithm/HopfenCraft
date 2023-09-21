package com.hugorithm.hopfencraft.exception.product;

public class ProductAlreadyExistsException extends RuntimeException{
    public ProductAlreadyExistsException(String message, Object... args) {
        super(String.format(message, args));
    }
}

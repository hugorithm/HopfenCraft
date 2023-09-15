package com.hugorithm.hopfencraft.exception.product;

public class ProductUpdateException extends RuntimeException{
    public ProductUpdateException(String message, Object... args) {
        super(String.format(message, args));
    }
}

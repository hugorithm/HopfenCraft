package com.hugorithm.hopfencraft.dto;

import java.util.Objects;

public class CartRegistrationDTO {
    private Long productId;
    private int quantity;

    public CartRegistrationDTO() {
    }

    public CartRegistrationDTO(Long productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartRegistrationDTO that = (CartRegistrationDTO) o;
        return quantity == that.quantity && Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, quantity);
    }

    @Override
    public String toString() {
        return "CartRegistrationDTO{" +
                "productId=" + productId +
                ", quantity=" + quantity +
                '}';
    }
}

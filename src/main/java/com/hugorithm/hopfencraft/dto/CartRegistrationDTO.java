package com.hugorithm.hopfencraft.dto;

import com.hugorithm.hopfencraft.model.CartItem;

import java.util.Objects;

public class CartRegistrationDTO {
    private Long productId;
    private int quantity;

    private Long cartItemId;

    public CartRegistrationDTO() {
    }

    public CartRegistrationDTO(Long productId, int quantity, CartItem cartItem, Long cartItemId) {
        this.productId = productId;
        this.quantity = quantity;
        this.cartItemId = cartItemId;
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

    public Long getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(Long cartItemId) {
        this.cartItemId = cartItemId;
    }
}

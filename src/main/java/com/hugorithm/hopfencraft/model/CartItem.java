package com.hugorithm.hopfencraft.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "carts")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long cartId;
    @ManyToOne
    private Product product;

    @ManyToOne
    @JsonIgnore
    private ApplicationUser user;

    private int quantity;

    public CartItem() {
    }

    public CartItem(Long cartId, Product product, ApplicationUser user, int quantity) {
        this.cartId = cartId;
        this.product = product;
        this.user = user;
        this.quantity = quantity;
    }

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ApplicationUser getUser() {
        return user;
    }

    public void setUser(ApplicationUser user) {
        this.user = user;
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
        CartItem cartItem = (CartItem) o;
        return quantity == cartItem.quantity && Objects.equals(cartId, cartItem.cartId) && Objects.equals(product, cartItem.product) && Objects.equals(user, cartItem.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cartId, product, user, quantity);
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "cartId=" + cartId +
                ", product=" + product +
                ", user=" + user +
                ", quantity=" + quantity +
                '}';
    }
}

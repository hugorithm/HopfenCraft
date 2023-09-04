package com.hugorithm.hopfencraft.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "carts")
@NoArgsConstructor
@Data
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long cartItemId;
    @ManyToOne
    private Product product;
    @ManyToOne
    @JsonIgnore
    private ApplicationUser user;
    private int quantity;
    private LocalDateTime addedDateTime;

    public CartItem(Product product, ApplicationUser user, int quantity, LocalDateTime addedDateTime) {
        this.product = product;
        this.user = user;
        this.quantity = quantity;
        this.addedDateTime = addedDateTime;
    }
}

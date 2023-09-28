package com.hugorithm.hopfencraft.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cart_items")
@NoArgsConstructor
@Data
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_item_id")
    private Long cartItemId;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    @ManyToOne
    private ApplicationUser user;
    private int quantity;
    @CreationTimestamp
    private LocalDateTime addedDateTime;
    private BigDecimal total;

    public CartItem(Product product, ApplicationUser user, int quantity, BigDecimal total) {
        this.product = product;
        this.user = user;
        this.quantity = quantity;
        this.total = total;
    }
}

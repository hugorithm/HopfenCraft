package com.hugorithm.hopfencraft.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

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
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private Order order;
    private int quantity;
    @CreationTimestamp
    private LocalDateTime addedDateTime;

    public CartItem(Product product, ApplicationUser user, int quantity) {
        this.product = product;
        this.user = user;
        this.quantity = quantity;
    }
}

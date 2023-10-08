package com.hugorithm.hopfencraft.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemId;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    private int quantity;
    @CreationTimestamp
    private LocalDateTime addedDateTime;
    private BigDecimal total;


    public OrderItem(Order order, Product product, int quantity, LocalDateTime addedDateTime, BigDecimal total) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.addedDateTime = addedDateTime;
        this.total = total;
    }
}

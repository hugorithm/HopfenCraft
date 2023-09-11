package com.hugorithm.hopfencraft.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@NoArgsConstructor
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private ApplicationUser user;
    private BigDecimal total;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<CartItem> orderItems;

    @Column(name = "order_date")
    @CreationTimestamp
    private LocalDateTime orderDate;

    public Order(ApplicationUser user, BigDecimal total) {
        this.user = user;
        this.total = total;
        this.orderItems = new ArrayList<>();
    }

    public void setOrderItems(List<CartItem> orderItems) {
        this.orderItems.clear(); // Clear existing references
        this.orderItems.addAll(orderItems); // Add the new order items
    }
}

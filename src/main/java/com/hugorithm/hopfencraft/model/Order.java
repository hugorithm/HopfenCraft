package com.hugorithm.hopfencraft.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    @OneToMany(mappedBy = "order")
    private List<CartItem> orderItems;

    @Column(name = "order_date")
    @CreationTimestamp
    private LocalDateTime orderDate;

    public Order(ApplicationUser user, BigDecimal total, List<CartItem> orderItems, LocalDateTime orderDate) {
        this.user = user;
        this.total = total;
        this.orderItems = orderItems;
        this.orderDate = orderDate;
    }
}

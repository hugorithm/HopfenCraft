package com.hugorithm.hopfencraft.model;

import com.hugorithm.hopfencraft.enums.OrderStatus;
import com.hugorithm.hopfencraft.enums.PaymentMethod;
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
    private ApplicationUser user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<CartItem> orderItems;

    @Column(name = "order_date")
    @CreationTimestamp
    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    @Column(name = "payment_transaction_id")
    private String paymentTransactionId;

    // Shipping Address fields
    private String shippingName;
    private String shippingAddress;
    private String shippingCity;
    private String shippingState;
    private String shippingPostalCode;
    private String shippingCountry;

    // Billing Address fields
    private String billingName;
    private String billingAddress;
    private String billingCity;
    private String billingState;
    private String billingPostalCode;
    private String billingCountry;

    //TODO: Delete this field
    private BigDecimal total;
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

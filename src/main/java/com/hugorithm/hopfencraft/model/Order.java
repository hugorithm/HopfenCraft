package com.hugorithm.hopfencraft.model;

import com.hugorithm.hopfencraft.enums.Currency;
import com.hugorithm.hopfencraft.enums.OrderStatus;
import com.hugorithm.hopfencraft.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
    @Column(name = "payment_transaction_status")
    private String paymentTransactionStatus;
    @Column(name = "payment_transaction_date")
    private LocalDateTime paymentTransactionDate;

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

    private BigDecimal total;
    @Enumerated(EnumType.STRING)
    private Currency currency;

    public Order(ApplicationUser user, BigDecimal total) {
        this.user = user;
        this.total = total;
    }

}

package com.hugorithm.hopfencraft.model;

import com.hugorithm.hopfencraft.enums.Currency;
import com.hugorithm.hopfencraft.enums.OrderStatus;
import com.hugorithm.hopfencraft.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
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
    private ApplicationUser user;
    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems;
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
    @OneToOne
    @JoinColumn(name = "shipping_details_id", referencedColumnName = "shipping_details_id")
    private ShippingDetails shippingDetails;

    private BigDecimal total;
    @Enumerated(EnumType.STRING)
    @Getter
    private static final Currency currency = Currency.EUR;

    public Order(ApplicationUser user, BigDecimal total, OrderStatus orderStatus) {
        this.user = user;
        this.total = total;
        this.orderStatus = orderStatus;
    }

}

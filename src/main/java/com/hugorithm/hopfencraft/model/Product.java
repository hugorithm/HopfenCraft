package com.hugorithm.hopfencraft.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;
    private String brand;
    @Column(unique = true)
    private String name;
    private String description;
    private int quantity;
    private BigDecimal price;
    @CreationTimestamp
    private LocalDateTime registerDateTime;
    @ManyToOne
    private ApplicationUser user;

    public Product(String brand, String name, String description, int quantity, BigDecimal price, ApplicationUser user) {
        this.brand = brand;
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.user = user;
    }

    public Product(Long productId, String brand, String name, String description, int quantity, BigDecimal price) {
        this.productId = productId;
        this.brand = brand;
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
    }
}

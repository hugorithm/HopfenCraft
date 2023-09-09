package com.hugorithm.hopfencraft.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
    @UpdateTimestamp
    private LocalDateTime registerDateTime;


    public Product(String brand, String name, String description, int quantity, BigDecimal price) {
        this.brand = brand;
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
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

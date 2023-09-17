package com.hugorithm.hopfencraft.model;

import com.hugorithm.hopfencraft.enums.Currency;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;

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
    private String sku;
    @NotBlank
    private String brand;
    @Column(unique = true)
    private String name;
    private String description;
    private int stockQuantity;
    private BigDecimal price;
    @Enumerated(EnumType.STRING)
    private Currency currency;
    @CreationTimestamp
    private LocalDateTime registerDateTime;
    @ManyToOne
    private ApplicationUser user;

    public Product(String brand, String name, String description, int quantity, BigDecimal price, Currency currency, ApplicationUser user) {
        this.brand = brand;
        this.name = name;
        this.description = description;
        this.stockQuantity = quantity;
        this.price = price;
        this.user = user;
        this.currency = currency;
        this.sku = generateProductCode(brand, name);
    }

    public Product(Long productId, String brand, String name, String description, int quantity, BigDecimal price, Currency currency) {
        this.productId = productId;
        this.brand = brand;
        this.name = name;
        this.description = description;
        this.stockQuantity = quantity;
        this.price = price;
        this.currency = currency;
        this.sku = generateProductCode(brand, name);
    }

    private String generateProductCode(String brand, String name){
        String replacedName = name.replaceAll("\\s", "");
        String replacedBrand = brand.replaceAll("\\s", "");
        return replacedBrand.substring(0, 2).toUpperCase() +
                generateRandomNumber() +
                replacedName.substring(replacedName.length() - 3).toUpperCase() +
                generateRandomNumber();
    }

    private String generateRandomNumber() {
        Random random = new Random();
        int randomNumber = random.nextInt(100);
        return String.format("%02d", randomNumber);
    }
}

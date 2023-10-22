package com.hugorithm.hopfencraft.model;

import com.hugorithm.hopfencraft.enums.Currency;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;

@Entity
@Table(name = "products")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;
    private String sku;
    @NotBlank
    private String brand;
    private String name;
    @Column(unique = true)
    private String brandName;
    private String description;
    private int stockQuantity;
    private BigDecimal price;
    @Enumerated(EnumType.STRING)
    @Getter
    private static final Currency currency = Currency.EUR;
    @CreationTimestamp
    private LocalDateTime registerDateTime;
    private LocalDateTime updatedDateTime;
    @ManyToOne
    private ApplicationUser user;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_image_id", referencedColumnName = "id")
    private ProductImage image;

    public Product(String brand, String name, String description, int quantity, BigDecimal price, ApplicationUser user, ProductImage image) {
        this.brand = brand;
        this.name = name;
        this.description = description;
        this.stockQuantity = quantity;
        this.price = price;
        this.user = user;
        this.image = image;
        this.sku = generateProductCode(brand, name);
        this.brandName = this.brand + this.name;
    }

    public Product(String brand, String name, String description, int quantity, BigDecimal price, ApplicationUser user) {
        this.brand = brand;
        this.name = name;
        this.description = description;
        this.stockQuantity = quantity;
        this.price = price;
        this.user = user;
        this.sku = generateProductCode(brand, name);
        this.brandName = this.brand + this.name;
    }

    public Product(Long productId, String brand, String name, String description, int quantity, BigDecimal price) {
        this.productId = productId;
        this.brand = brand;
        this.name = name;
        this.description = description;
        this.stockQuantity = quantity;
        this.price = price;
        this.sku = generateProductCode(brand, name);
        this.brandName = this.brand + this.name;
    }

    private String generateProductCode(String brand, String name) {
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

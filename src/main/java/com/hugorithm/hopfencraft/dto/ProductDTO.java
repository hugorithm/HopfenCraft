package com.hugorithm.hopfencraft.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class ProductDTO {
    private Long productId;
    private String brand;
    private String name;
    private String description;
    private int quantity;
    private BigDecimal price;
    private LocalDateTime registerDateTime;

    public ProductDTO(String brand, String name, String description, int quantity, BigDecimal price) {
        this.brand = brand;
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
    }
}

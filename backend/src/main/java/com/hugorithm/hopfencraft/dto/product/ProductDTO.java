package com.hugorithm.hopfencraft.dto.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hugorithm.hopfencraft.enums.Currency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductDTO {
    private Long productId;
    private String brand;
    private String name;
    private String description;
    private int quantity;
    private String sku;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal price;
    private Currency currency;
    private LocalDateTime registerDateTime;
    public ProductDTO(String brand, String name, String description, int quantity, BigDecimal price) {
        this.brand = brand;
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
    }
}

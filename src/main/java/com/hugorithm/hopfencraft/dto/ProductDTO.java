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
}

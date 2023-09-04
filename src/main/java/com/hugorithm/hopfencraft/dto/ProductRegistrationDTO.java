package com.hugorithm.hopfencraft.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRegistrationDTO {
    private String brand;
    private String name;
    private String description;
    private int quantity;
    private BigDecimal price;
}

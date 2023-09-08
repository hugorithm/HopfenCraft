package com.hugorithm.hopfencraft.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@AllArgsConstructor
@Data
public class ProductUpdateDTO {
    private Long productId;
    private String brand;
    private String name;
    private String description;
    private int quantity;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal price;
}

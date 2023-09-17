package com.hugorithm.hopfencraft.dto.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductUpdateDTO {
    @Positive
    @NotNull
    private Long productId;
    private String brand;
    private String name;
    private String description;
    private Integer quantity;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal price;
}

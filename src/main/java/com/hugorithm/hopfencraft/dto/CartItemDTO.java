package com.hugorithm.hopfencraft.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class CartItemDTO {
    private Long cartItemId;
    private ProductDTO product;
    private int quantity;
    private LocalDateTime addedDateTime;
}

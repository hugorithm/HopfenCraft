package com.hugorithm.hopfencraft.dto;

import com.hugorithm.hopfencraft.model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class CartItemDTO {
    private Long cartItemId;
    private Product product;
    private int quantity;
    private LocalDateTime addedDateTime;
}

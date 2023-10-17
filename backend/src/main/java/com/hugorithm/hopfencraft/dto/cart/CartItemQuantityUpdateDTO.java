package com.hugorithm.hopfencraft.dto.cart;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CartItemQuantityUpdateDTO {
    @Positive
    private Long cartItemId;
    @Positive
    private int quantity;
}

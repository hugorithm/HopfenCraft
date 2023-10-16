package com.hugorithm.hopfencraft.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CartItemQuantityUpdateDTO {
    private Long cartItemId;
    private int quantity;
}

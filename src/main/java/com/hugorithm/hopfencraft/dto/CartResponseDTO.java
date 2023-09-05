package com.hugorithm.hopfencraft.dto;

import com.hugorithm.hopfencraft.model.CartItem;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CartResponseDTO {
    private List<CartItem> cartItems;
}

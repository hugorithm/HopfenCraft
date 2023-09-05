package com.hugorithm.hopfencraft.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CartResponseDTO {
    private List<CartItemDTO> cartItems;
}

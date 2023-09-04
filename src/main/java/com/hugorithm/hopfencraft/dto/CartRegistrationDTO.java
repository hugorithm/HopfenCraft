package com.hugorithm.hopfencraft.dto;

import com.hugorithm.hopfencraft.model.CartItem;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartRegistrationDTO {
    private Long productId;
    private int quantity;
    private Long cartItemId;
    private List<CartItem> cartItems;

    public CartRegistrationDTO(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }
}

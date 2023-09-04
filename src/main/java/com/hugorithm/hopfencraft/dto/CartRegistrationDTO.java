package com.hugorithm.hopfencraft.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartRegistrationDTO {
    private Long productId;
    private int quantity;
    private Long cartItemId;
}

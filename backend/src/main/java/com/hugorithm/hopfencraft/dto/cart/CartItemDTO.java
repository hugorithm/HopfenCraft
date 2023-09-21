package com.hugorithm.hopfencraft.dto.cart;

import com.hugorithm.hopfencraft.dto.product.ProductDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartItemDTO {
    private Long cartItemId;
    private ProductDTO product;
    private int quantity;
    private LocalDateTime addedDateTime;
}

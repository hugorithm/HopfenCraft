package com.hugorithm.hopfencraft.dto.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hugorithm.hopfencraft.dto.cart.CartItemDTO;
import com.hugorithm.hopfencraft.enums.Currency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {
    private Long orderId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal total;
    private String currency;
    private List<CartItemDTO> cartItems;
    private LocalDateTime orderDate;
}

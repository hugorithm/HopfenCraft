package com.hugorithm.hopfencraft.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal total;
    private List<CartItemDTO> cartItems;
    private LocalDateTime orderDate;
}

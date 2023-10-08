package com.hugorithm.hopfencraft.dto.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hugorithm.hopfencraft.dto.product.ProductDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderItemDTO {
    private Long orderItemId;
    private ProductDTO product;
    private int quantity;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal total;
    private LocalDateTime addedDateTime;
}

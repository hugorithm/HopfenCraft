package com.hugorithm.hopfencraft.dto.paypal;

import com.hugorithm.hopfencraft.dto.order.OrderDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDTO {
    @NotBlank
    private String currency;
    @NotBlank
    private String total;
    @NotBlank
    private OrderDTO orderDTO;
}

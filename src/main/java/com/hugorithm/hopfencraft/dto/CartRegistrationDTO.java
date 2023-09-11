package com.hugorithm.hopfencraft.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartRegistrationDTO {
    @Positive
    @NotNull
    private Long productId;
    @Positive
    @NotNull
    private int quantity;
}

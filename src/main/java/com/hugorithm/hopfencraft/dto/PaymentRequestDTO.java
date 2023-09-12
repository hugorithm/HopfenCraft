package com.hugorithm.hopfencraft.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDTO {
    @NotBlank
    private String total;
    @NotBlank
    private String currency;
    @NotBlank
    private String method;
    @NotBlank
    private String intent;
    @NotBlank
    private String description;
    @NotBlank
    private String successUrl;
    @NotBlank
    private String cancelUrl;
}

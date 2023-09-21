package com.hugorithm.hopfencraft.dto.order;

import com.hugorithm.hopfencraft.enums.Currency;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreationDTO {
    @NotBlank
    private Currency currency;
}

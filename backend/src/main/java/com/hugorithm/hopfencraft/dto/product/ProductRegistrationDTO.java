package com.hugorithm.hopfencraft.dto.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hugorithm.hopfencraft.enums.Currency;
import com.hugorithm.hopfencraft.validators.PositiveBigDecimal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRegistrationDTO {
    @NotBlank
    private String brand;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @Positive
    @NotNull
    private int quantity;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @PositiveBigDecimal
    @NotNull
    private BigDecimal price;
    private Currency currency;
    private MultipartFile image;
}

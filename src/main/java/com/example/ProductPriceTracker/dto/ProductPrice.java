package com.example.ProductPriceTracker.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductPrice {

    @NotBlank
    private String productUrl;

    @NotBlank
    private double currentPrice;
}

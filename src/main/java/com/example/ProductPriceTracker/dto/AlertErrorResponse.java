package com.example.ProductPriceTracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AlertErrorResponse {
    private int status;
    private String message;
    private long timestamp;
}

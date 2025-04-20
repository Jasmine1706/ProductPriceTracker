package com.example.ProductPriceTracker.exceptions;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

public class BadAlertRequestException extends RuntimeException {

    public BadAlertRequestException(String message){
        super(message);
    }
}

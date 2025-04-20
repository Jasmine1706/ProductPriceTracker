package com.example.ProductPriceTracker.exceptions;

import com.example.ProductPriceTracker.dto.AlertErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadAlertRequestException.class)
    public ResponseEntity<AlertErrorResponse> invalidRequestException(BadAlertRequestException e) {
        AlertErrorResponse error =
                new AlertErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage().substring(0,60), System.currentTimeMillis());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}

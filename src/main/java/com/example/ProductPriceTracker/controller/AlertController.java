package com.example.ProductPriceTracker.controller;

import com.example.ProductPriceTracker.dto.AlertRequest;
import com.example.ProductPriceTracker.dto.AlertResponse;
import com.example.ProductPriceTracker.exceptions.BadAlertRequestException;
import com.example.ProductPriceTracker.service.AlertService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/alerts")
@Slf4j
public class AlertController {

    @Autowired
    private AlertService alertService;

    @PostMapping
    public AlertResponse saveAlert(@RequestBody AlertRequest request)  {
        try {
            log.info("Saving new alert in database.");
            alertService.saveAlert(request);
        } catch (BadAlertRequestException exception){
            log.error("Failed saving new alert.");
            throw new BadAlertRequestException(exception.getMessage());
        }
        log.info("Alert set successfully.");
        return AlertResponse.getAlertResponse(request);
    }
}

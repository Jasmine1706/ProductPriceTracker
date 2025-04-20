package com.example.ProductPriceTracker.service;

import com.example.ProductPriceTracker.dto.AlertRequest;
import com.example.ProductPriceTracker.dto.ProductPrice;
import com.example.ProductPriceTracker.exceptions.BadAlertRequestException;
import com.example.ProductPriceTracker.model.Alert;
import com.example.ProductPriceTracker.repository.AlertRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.example.ProductPriceTracker.util.CommonUtils.cronDateTimeConverter;


@Service
@Slf4j
public class AlertService {

    @Autowired
    private AlertRepository alertRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ProductService productService;

    public AlertService(AlertRepository alertRepository, EmailService emailService, ProductService productService) {
        this.alertRepository = alertRepository;
        this.emailService = emailService;
        this.productService = productService;
    }

    public void saveAlert(AlertRequest request) {
        try {
            alertRepository.save(AlertRequest.convertUserCreateRequest(request));
        } catch (Exception exception) {
            throw new BadAlertRequestException(exception.getMessage());
        }

    }

    //0 0 * * * *
    @Scheduled(cron = "0 0/1 * * * *")
    public void checkAlertsForNotification() {
        log.info("Cron Started....");
        List<Alert> alerts = null;
        Map<String, ProductPrice> mapOfProducts = null;
        try {
            log.info("Fetching alert list...");
            alerts = getAllAlerts();
            if(alerts.isEmpty()){
                log.error("No Alerts Found in Database.");
            }
        } catch (Exception exception) {
            exception.getMessage();
            log.error("Error while fetching Alert List from Local Database");
        }

        try {
            log.info("Fetching list of products prices  available from product_prices.json");
            mapOfProducts = productService.loadProductsFromFile();
        } catch (Exception exception) {
            exception.getMessage();
            log.error("Error while fetching list of products from product_prices.json");
        }

        try {
            if (!Objects.isNull(mapOfProducts) && !alerts.isEmpty()) {
                for (Alert value : alerts) {
                    AlertRequest alert = AlertService.toAlertRequest((Alert) value);
                    ProductPrice productPrice = mapOfProducts.get(alert.getProductUrl());
                    log.info("Check for Mail trigger");
                    if (productPrice.getCurrentPrice() <= alert.getDesiredPrice()
                            && Boolean.TRUE.equals(cronDateTimeConverter(alert.getFrequency()))) {
                        emailService.notifyUser(productPrice, alert, this);
                       // alertRepository.delete(value);
                        log.info("Mail Sent and alert deleted from database");
                    }
                }
            }
        } catch (Exception exception) {
            exception.getMessage();
            log.error("Error sending mail to the user.");
        }

    }

    private List<Alert> getAllAlerts() {
        return alertRepository.findAll();
    }


    public static AlertRequest toAlertRequest(Alert alert) {
        AlertRequest request = new AlertRequest();
        request.setProductUrl(alert.getProductUrl());
        request.setDesiredPrice(alert.getDesiredPrice());
        request.setFrequency(alert.getCheckFrequency());
        request.setEmail(alert.getEmail());
        return request;
    }
}
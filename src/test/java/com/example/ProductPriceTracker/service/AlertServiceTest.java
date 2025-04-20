package com.example.ProductPriceTracker.service;

import com.example.ProductPriceTracker.dto.AlertRequest;
import com.example.ProductPriceTracker.dto.ProductPrice;
import com.example.ProductPriceTracker.exceptions.BadAlertRequestException;
import com.example.ProductPriceTracker.model.Alert;
import com.example.ProductPriceTracker.repository.AlertRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class AlertServiceTest {

    @Mock
    private AlertRepository alertRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private ProductService productService;

    @InjectMocks
    private AlertService alertService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ----------- Test saveAlert() -----------

    @Test
    public void saveAlert_shouldSaveAlertSuccessfully() {
        AlertRequest request = new AlertRequest("http://example.com", 999.0, "daily", "test@example.com");
        Alert alert = AlertRequest.convertUserCreateRequest(request);

        when(alertRepository.save(any(Alert.class))).thenReturn(alert);

        assertDoesNotThrow(() -> alertService.saveAlert(request));
        verify(alertRepository).save(any(Alert.class));
    }

    @Test
    public void saveAlert_shouldThrowBadAlertRequestException_whenRepoFails() {
        AlertRequest request = new AlertRequest("http://example.com", 999.0, "daily", "test@example.com");

        when(alertRepository.save(any(Alert.class))).thenThrow(new RuntimeException("DB error"));

        BadAlertRequestException ex = assertThrows(BadAlertRequestException.class, () -> alertService.saveAlert(request));
        assertEquals("DB error", ex.getMessage());
    }

    // ----------- Test checkAlertsForNotification() -----------

    @Test
    public void checkAlertsForNotification_shouldSendEmailAndDeleteAlert() {
        Alert alert = new Alert(1, "http://example.com", 500.0, "daily", "user@example.com",Date.from(Instant.now()),Date.from(Instant.now()));
        AlertRequest alertRequest = AlertService.toAlertRequest(alert);
        ProductPrice productPrice = new ProductPrice("http://example.com", 400.0);

        when(alertRepository.findAll()).thenReturn(List.of(alert));
        when(productService.loadProductsFromFile()).thenReturn(Map.of("http://example.com", productPrice));

        // Mock static util method
        try (MockedStatic<com.example.ProductPriceTracker.util.CommonUtils> utilsMock = mockStatic(com.example.ProductPriceTracker.util.CommonUtils.class)) {
            utilsMock.when(() -> com.example.ProductPriceTracker.util.CommonUtils.cronDateTimeConverter("daily")).thenReturn(true);

            alertService.checkAlertsForNotification();

            verify(emailService).notifyUser(eq(productPrice), any(AlertRequest.class), any(AlertService.class));
            verify(alertRepository).delete(alert);
        }
    }

    @Test
    public void checkAlertsForNotification_shouldNotSendEmail_ifPriceIsHigher() {
        Alert alert = new Alert(1, "http://example.com", 300.0, "daily", "user@example.com",Date.from(Instant.now()),Date.from(Instant.now()));
        ProductPrice productPrice = new ProductPrice("http://example.com", 500.0);

        when(alertRepository.findAll()).thenReturn(List.of(alert));
        when(productService.loadProductsFromFile()).thenReturn(Map.of("http://example.com", productPrice));

        try (MockedStatic<com.example.ProductPriceTracker.util.CommonUtils> utilsMock = mockStatic(com.example.ProductPriceTracker.util.CommonUtils.class)) {
            utilsMock.when(() -> com.example.ProductPriceTracker.util.CommonUtils.cronDateTimeConverter("daily")).thenReturn(true);

            alertService.checkAlertsForNotification();

            verify(emailService, never()).notifyUser(any(), any(), any());
            verify(alertRepository, never()).delete(any());
        }
    }

    @Test
    public void checkAlertsForNotification_shouldNotSendEmail_ifFrequencyCheckFails() {
        Alert alert = new Alert(1, "http://example.com", 500.0, "daily", "user@example.com", Date.from(Instant.now()),Date.from(Instant.now()));
        ProductPrice productPrice = new ProductPrice("http://example.com", 400.0);

        when(alertRepository.findAll()).thenReturn(List.of(alert));
        when(productService.loadProductsFromFile()).thenReturn(Map.of("http://example.com", productPrice));

        try (MockedStatic<com.example.ProductPriceTracker.util.CommonUtils> utilsMock = mockStatic(com.example.ProductPriceTracker.util.CommonUtils.class)) {
            utilsMock.when(() -> com.example.ProductPriceTracker.util.CommonUtils.cronDateTimeConverter("daily")).thenReturn(false);

            alertService.checkAlertsForNotification();

            verify(emailService, never()).notifyUser(any(), any(), any());
            verify(alertRepository, never()).delete(any());
        }
    }

    @Test
    public void checkAlertsForNotification_shouldHandleExceptionWhileFetchingAlerts() {
        when(alertRepository.findAll()).thenThrow(new RuntimeException("DB failure"));

        assertDoesNotThrow(() -> alertService.checkAlertsForNotification());

        // Logs the error, no exception thrown
    }

    @Test
    public void checkAlertsForNotification_shouldHandleExceptionWhileLoadingProducts() {
        Alert alert = new Alert(1,"http://example.com", 500.0, "daily", "user@example.com",Date.from(Instant.now()),Date.from(Instant.now()));

        when(alertRepository.findAll()).thenReturn(List.of(alert));
        when(productService.loadProductsFromFile()).thenThrow(new RuntimeException("File error"));

        assertDoesNotThrow(() -> alertService.checkAlertsForNotification());
    }

    @Test
    public void checkAlertsForNotification_shouldHandleExceptionDuringNotification() {
        Alert alert = new Alert(1, "http://example.com", 500.0, "daily", "user@example.com",Date.from(Instant.now()),Date.from(Instant.now()));
        ProductPrice productPrice = new ProductPrice("http://example.com", 400.0);

        when(alertRepository.findAll()).thenReturn(List.of(alert));
        when(productService.loadProductsFromFile()).thenReturn(Map.of("http://example.com", productPrice));

        doThrow(new RuntimeException("Mail failed")).when(emailService).notifyUser(any(), any(), any());

        try (MockedStatic<com.example.ProductPriceTracker.util.CommonUtils> utilsMock = mockStatic(com.example.ProductPriceTracker.util.CommonUtils.class)) {
            utilsMock.when(() -> com.example.ProductPriceTracker.util.CommonUtils.cronDateTimeConverter("daily")).thenReturn(true);

            assertDoesNotThrow(() -> alertService.checkAlertsForNotification());
        }
    }
}

package com.example.ProductPriceTracker.service;

import com.example.ProductPriceTracker.dto.ProductPrice;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void loadProductsFromFile_shouldReturnProductMap_whenFileIsValid() throws IOException {
        // Arrange
        ProductPrice p1 = new ProductPrice("url1", 500.0);
        ProductPrice p2 = new ProductPrice("url2", 600.0);
        ProductPrice[] products = new ProductPrice[]{p1, p2};

        File mockFile = new File("src/main/resources/product_prices.json");
        when(objectMapper.readValue(eq(mockFile), eq(ProductPrice[].class))).thenReturn(products);

        // Act
        Map<String, ProductPrice> result = productService.loadProductsFromFile();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.containsKey("url1"));
        assertTrue(result.containsKey("url2"));
        //assertEquals("Product 1", result.get("url1").getProductTitle());
    }

    @Test
    public void loadProductsFromFile_shouldReturnEmptyMap_whenIOExceptionOccurs() throws IOException {
        // Arrange
        File mockFile = new File("src/main/resources/product_prices.json");
        when(objectMapper.readValue(eq(mockFile), eq(ProductPrice[].class))).thenThrow(new IOException("File error"));

        // Act
        Map<String, ProductPrice> result = productService.loadProductsFromFile();

        // Assert
        assertTrue(result.isEmpty());
    }
}

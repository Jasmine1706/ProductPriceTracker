package com.example.ProductPriceTracker.service;

import com.example.ProductPriceTracker.dto.ProductPrice;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
@Slf4j
public class ProductService {

    @Autowired
    ObjectMapper objectMapper;

    public Map<String,ProductPrice> loadProductsFromFile() {
        try {
            File file = new File("src/main/resources/product_prices.json");
            ProductPrice[] products = objectMapper.readValue(file, ProductPrice[].class);
            Map<String,ProductPrice> mapOfProducts = new HashMap<>();
            for(ProductPrice productPrice : products){
                mapOfProducts.put(productPrice.getProductUrl(),productPrice);
            }
            return mapOfProducts;
        } catch (IOException e) {
            e.getMessage();
            log.error("Error in loadProductsFromFile() in ProductService");
            return Collections.emptyMap();
        }
    }
}

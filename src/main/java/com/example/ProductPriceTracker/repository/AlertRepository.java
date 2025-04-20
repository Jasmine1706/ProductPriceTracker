package com.example.ProductPriceTracker.repository;

import com.example.ProductPriceTracker.model.Alert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertRepository extends JpaRepository<Alert, Integer> {

   /* @Modifying
    @Query("value = DELETE FROM alert a where f.id = ?1")
    void deleteAlert();*/
}

package com.example.ProductPriceTracker.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlertResponse {

    @NotBlank
    private String message;

    @NotBlank
    private String email;

    public static AlertResponse getAlertResponse(AlertRequest alertRequest){
        String productId = alertRequest.getProductUrl().substring(alertRequest.getProductUrl().lastIndexOf("/") + 1);
        return AlertResponse.builder()
                .message("Alert set successfully for product:"+productId+
                        ". You will be notified on below mail once your product price is less than or equal to "
                        +alertRequest.getDesiredPrice()+ ", Thank you.")
                .email(alertRequest.getEmail()).build();
    }
}

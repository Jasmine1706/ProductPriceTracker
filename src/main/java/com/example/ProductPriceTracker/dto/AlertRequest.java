package com.example.ProductPriceTracker.dto;

import com.example.ProductPriceTracker.model.Alert;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import static com.example.ProductPriceTracker.util.CommonUtils.cronExpression;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlertRequest {

    @NotBlank
    private String productUrl;

    @NotBlank
    private double desiredPrice;

    @NotBlank
    private String email;

    private String frequency;

    public static Alert convertUserCreateRequest(AlertRequest request){
        return Alert.builder()
                .productUrl(request.getProductUrl())
                .desiredPrice(request.getDesiredPrice())
                .checkFrequency(null != request.getFrequency() ? cronExpression(request.getFrequency()) : "")
                .email(request.getEmail())
                .build();
    }

}

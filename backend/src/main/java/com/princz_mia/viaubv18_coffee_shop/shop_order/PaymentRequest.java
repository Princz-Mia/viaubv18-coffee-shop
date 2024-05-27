package com.princz_mia.viaubv18_coffee_shop.shop_order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentRequest {
    @NotNull(message = "Shop Order Id cannot be null")
    private Long shopOrderId;
    @NotEmpty(message = "Payment Id cannot be null or empty")
    private String paymentId;
}
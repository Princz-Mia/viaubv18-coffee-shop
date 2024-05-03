package com.princz_mia.viaubv18_coffee_shop.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShopOrderRequest {
    @NotEmpty(message = "User Id cannot be null or empty")
    private Long userId;
    @NotEmpty(message = "Address Id cannot be null or empty")
    private Long addressId;
    @NotEmpty(message = "Shipping Method Id cannot be null or empty")
    private Long shippingMethodId;
    @NotEmpty(message = "Date cannot be null or empty")
    private LocalDateTime orderDate;
    @NotEmpty(message = "Total price cannot be null or empty")
    private Double orderTotal;
    @NotEmpty(message = "Firstname cannot be null or empty")
    private String firstName;
    @NotEmpty(message = "Lastname cannot be null or empty")
    private String lastName;
    @NotEmpty(message = "Phone number cannot be null or empty")
    private String phoneNumber;
}
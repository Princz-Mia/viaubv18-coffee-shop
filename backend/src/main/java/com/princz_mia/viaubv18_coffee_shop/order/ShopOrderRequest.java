package com.princz_mia.viaubv18_coffee_shop.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShopOrderRequest {
    @NotNull(message = "User Id cannot be null")
    private Long userId;
    @NotNull(message = "Address Id cannot be null")
    private Long addressId;
    @NotNull(message = "Shipping Method Id cannot be null")
    private Long shippingMethodId;
    @NotNull(message = "Date cannot be null")
    private LocalDateTime orderDate;
    @NotNull(message = "Total price cannot be null")
    private Double orderTotal;
    @NotEmpty(message = "Firstname cannot be null or empty")
    private String firstName;
    @NotEmpty(message = "Lastname cannot be null or empty")
    private String lastName;
    @NotEmpty(message = "Phone number cannot be null or empty")
    private String phoneNumber;
}
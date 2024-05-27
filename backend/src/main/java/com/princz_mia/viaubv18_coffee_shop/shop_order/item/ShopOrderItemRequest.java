package com.princz_mia.viaubv18_coffee_shop.shop_order.item;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShopOrderItemRequest {
    @NotNull(message = "Product Id cannot be null")
    private Long productId;
    @NotNull(message = "Shop Order Id cannot be null")
    private Long shopOrderId;
    @NotNull(message = "Price cannot be null")
    private Double price;
    @NotNull(message = "Quantity cannot be null")
    private Integer qty;
}
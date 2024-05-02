package com.princz_mia.viaubv18_coffee_shop.shopping_cart.item;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShoppingCartItemRequest {
    @NotNull(message = "Cart Id cannot be null")
    private Long cartId;
    @NotNull(message = "Product Id cannot be null")
    private Long productId;
    @NotNull(message = "Quantity cannot be null")
    private Integer qty;
}

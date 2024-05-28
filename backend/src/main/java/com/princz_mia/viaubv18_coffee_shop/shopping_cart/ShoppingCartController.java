package com.princz_mia.viaubv18_coffee_shop.shopping_cart;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
@Slf4j
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    @GetMapping(path = "/getById/{id}")
    public ResponseEntity<ShoppingCart> getShoppingCartById(@PathVariable(value = "id") @NotNull(message = "Shopping Cart Id must not be null") Long id) {
        ShoppingCart shoppingCartByUserId = shoppingCartService.getShoppingCartById(id);
        return ResponseEntity.ok(shoppingCartByUserId);
    }

    @GetMapping(path = "/getByUserId/{userId}")
    public ResponseEntity<ShoppingCart> getShoppingCartByUserId(@PathVariable(value = "userId") @NotNull(message = "User Id must not be null") Long userId) {
        ShoppingCart shoppingCartByUserId = shoppingCartService.getShoppingCartByUserId(userId);
        return ResponseEntity.ok(shoppingCartByUserId);
    }
}

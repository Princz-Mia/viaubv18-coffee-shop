package com.princz_mia.viaubv18_coffee_shop.shopping_cart.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cartItem")
@RequiredArgsConstructor
@Slf4j
public class ShoppingCartItemController {

    private final ShoppingCartItemService shoppingCartItemService;

    @GetMapping(path = "/getByCartId/{cartId}")
    public ResponseEntity<List<ShoppingCartItem>> getShoppingCartItemsByCartId(@PathVariable(value = "cartId") @NotNull(message = "Shopping Cart Id must not be null") Long cartId) {
        List<ShoppingCartItem> shoppingCartItems = shoppingCartItemService.getShoppingCartItemsByCartId(cartId);
        return ResponseEntity.ok(shoppingCartItems);
    }

    @PostMapping(path = "/add")
    public ResponseEntity<ShoppingCartItem> addShoppingCartItemToCart(@RequestBody @Valid ShoppingCartItemRequest shoppingCartItemRequest) {
        ShoppingCartItem savedShoppingCartItem = shoppingCartItemService.addShoppingCartItemToCart(shoppingCartItemRequest);
        return ResponseEntity.ok(savedShoppingCartItem);
    }

    @PostMapping(path = "/remove")
    public ResponseEntity<?> removeShoppingCartItemToCart(@RequestBody @Valid ShoppingCartItemRequest shoppingCartItemRequest) {
        boolean isRemovalFailed = shoppingCartItemService.removeShoppingCartItemToCart(shoppingCartItemRequest);

        if (isRemovalFailed)
            return new ResponseEntity<>("Failed removal.", HttpStatus.NOT_MODIFIED);

        return new ResponseEntity<>("Successful removal.", HttpStatus.ACCEPTED);
    }
}

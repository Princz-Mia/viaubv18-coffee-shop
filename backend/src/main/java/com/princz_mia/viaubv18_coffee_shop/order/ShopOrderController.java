package com.princz_mia.viaubv18_coffee_shop.order;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/shopOrder")
@RequiredArgsConstructor
@Slf4j
public class ShopOrderController {

    private final ShopOrderService shopOrderService;

    @PostMapping(path = "/create")
    public ResponseEntity<ShopOrder> createShopOrder(@RequestBody @Valid ShopOrderRequest shopOrderRequest) {
        ShopOrder shopOrder = shopOrderService.createShopOrder(shopOrderRequest);
        return ResponseEntity.ok(shopOrder);
    }

    @GetMapping(path = "/pending/{userId}")
    public ResponseEntity<ShopOrder> getPendingShopOrderByUserId(@PathVariable(value = "userId") Long userId) {
        ShopOrder shopOrder = shopOrderService.getPendingShopOrderByUserId(userId);
        return ResponseEntity.ok(shopOrder);
    }
}

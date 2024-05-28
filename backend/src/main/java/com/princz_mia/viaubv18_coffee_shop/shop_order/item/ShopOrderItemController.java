package com.princz_mia.viaubv18_coffee_shop.shop_order.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/shopOrderItem")
@RequiredArgsConstructor
@Slf4j
public class ShopOrderItemController {

    private final ShopOrderItemService shopOrderItemService;

    @PostMapping(path = "/create")
    public ResponseEntity<ShopOrderItem> createOrderItem(@RequestBody @Valid ShopOrderItemRequest shopOrderItemRequest) {
        ShopOrderItem shopOrderItem = shopOrderItemService.createOrderItem(shopOrderItemRequest);
        return ResponseEntity.ok(shopOrderItem);
    }

    @GetMapping(path = "/getByShopOrderId/{shopOrderId}")
    public ResponseEntity<List<ShopOrderItem>> getShopOrderItemsByShopOrderId(@PathVariable(value = "shopOrderId") @NotNull(message = "Shop Order Id must not be null") Long shopOrderId) {
        List<ShopOrderItem> shopOrderItems = shopOrderItemService.getShopOrderItemsByShopOrderId(shopOrderId);
        return ResponseEntity.ok(shopOrderItems);
    }
}

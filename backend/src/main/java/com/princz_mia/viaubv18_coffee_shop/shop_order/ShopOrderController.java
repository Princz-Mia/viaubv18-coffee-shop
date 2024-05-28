package com.princz_mia.viaubv18_coffee_shop.shop_order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping(path = "/getShopOrdersByUserId/{userId}")
    public ResponseEntity<List<ShopOrder>> getShopOrdersByUserId(@PathVariable(value = "userId") @NotNull(message = "User Id must not be null") Long userId) {
        List<ShopOrder> shopOrders = shopOrderService.getShopOrdersByUserId(userId);
        return ResponseEntity.ok(shopOrders);
    }

    @GetMapping(path = "/pending/{userId}")
    public ResponseEntity<ShopOrder> getPendingShopOrderByUserId(@PathVariable(value = "userId") @NotNull(message = "User Id must not be null") Long userId) {
        ShopOrder shopOrder = shopOrderService.getPendingShopOrderByUserId(userId);
        return ResponseEntity.ok(shopOrder);
    }

    @GetMapping(path = "/hasPendingShopOrder/{userId}")
    public ResponseEntity<Boolean> hasUserPendingShopOrder(@PathVariable(value = "userId") @NotNull(message = "User Id must not be null") Long userId) {
        Boolean hasPendingOrder = shopOrderService.hasUserPendingShopOrder(userId);
        return ResponseEntity.ok(hasPendingOrder);
    }

    @PostMapping(path = "/pay")
    public ResponseEntity<ShopOrder> processPayTransaction(@RequestBody @Valid PaymentRequest paymentRequest) {
        ShopOrder shopOrder = shopOrderService.processPayTransaction(paymentRequest);
        return ResponseEntity.ok(shopOrder);
    }

    @GetMapping(path = "/getByShopOrderId/{shopOrderId}")
    public ResponseEntity<ShopOrder> getShopOrderById(@PathVariable(value = "shopOrderId") @NotNull(message = "Shopping Order Id must not be null") Long shopOrderId) {
        ShopOrder shopOrder = shopOrderService.getShopOrderById(shopOrderId);
        return ResponseEntity.ok(shopOrder);
    }

    @GetMapping(path = "/pagination")
    public ResponseEntity<ShopOrderPageResponse> getPageOfShopOrders(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "8", required = false) int pageSize
    ) {
        ShopOrderPageResponse pageOfShopOrders = shopOrderService.getPageOfShopOrders(pageNumber, pageSize);
        return ResponseEntity.ok(pageOfShopOrders);
    }

    @PostMapping(path = "/updateShopOrderStatus")
    public ResponseEntity<ShopOrder> updateShopOrderStatus(@RequestBody @Valid ShopOrderRequest shopOrderRequest) {
        ShopOrder shopOrder = shopOrderService.updateShopOrderStatus(shopOrderRequest);
        return ResponseEntity.ok(shopOrder);
    }

    @PostMapping(path = "/deleteById/{shopOrderId}")
    public ResponseEntity<Boolean> deleteShopOrderById(@PathVariable(value = "shopOrderId") @NotNull(message = "Shopping Order Id must not be null") Long shopOrderId) {
        shopOrderService.deleteShopOrderById(shopOrderId);
        return ResponseEntity.ok(true);
    }
}

package com.princz_mia.viaubv18_coffee_shop.order.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/shopOrderItem")
@RequiredArgsConstructor
@Slf4j
public class OrderItemController {

    private final OrderItemService orderItemService;

    @PostMapping(path = "/create")
    public ResponseEntity<OrderItem> createOrderItem(@RequestBody @Valid OrderItemRequest orderItemRequest) {
        OrderItem orderItem = orderItemService.createOrderItem(orderItemRequest);
        return ResponseEntity.ok(orderItem);
    }

    @GetMapping(path = "/getByShopOrderId/{shopOrderId}")
    public ResponseEntity<List<OrderItem>> getShopOrderItemsByShopOrderId(@PathVariable Long shopOrderId) {
        List<OrderItem> orderItems = orderItemService.getShopOrderItemsByShopOrderId(shopOrderId);
        return ResponseEntity.ok(orderItems);
    }
}

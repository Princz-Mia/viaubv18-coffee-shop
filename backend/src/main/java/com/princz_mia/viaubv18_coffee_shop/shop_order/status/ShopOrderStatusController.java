package com.princz_mia.viaubv18_coffee_shop.shop_order.status;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/shopOrderStatus")
@RequiredArgsConstructor
@Slf4j
public class ShopOrderStatusController {

    private final ShopOrderStatusService shopOrderStatusService;

    @GetMapping(path = "/getAllStatus")
    public ResponseEntity<List<ShopOrderStatus>> getAllShopOrderStatuses() {
        List<ShopOrderStatus> shopOrderStatuses = shopOrderStatusService.getAllShopOrderStatuses();
        return ResponseEntity.ok(shopOrderStatuses);
    }
}

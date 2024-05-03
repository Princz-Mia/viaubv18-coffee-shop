package com.princz_mia.viaubv18_coffee_shop.shipping_method;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/shippingMethod")
@RequiredArgsConstructor
@Slf4j
public class ShippingMethodController {

    private final ShippingMethodService shippingMethodService;

    @GetMapping(path = "/all")
    public ResponseEntity<List<ShippingMethod>> getShippingMethods() {
        List<ShippingMethod> shippingMethods = shippingMethodService.getShippingMethods();
        return ResponseEntity.ok(shippingMethods);
    }
}

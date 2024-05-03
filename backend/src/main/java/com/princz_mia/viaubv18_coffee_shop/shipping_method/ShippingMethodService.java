package com.princz_mia.viaubv18_coffee_shop.shipping_method;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShippingMethodService {

    private final ShippingMethodRepository shippingMethodRepository;

    public List<ShippingMethod> getShippingMethods() {
        return shippingMethodRepository.findAll();
    }
}

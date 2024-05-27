package com.princz_mia.viaubv18_coffee_shop.shop_order.status;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShopOrderStatusService {

    private final ShopOrderStatusRepository shopOrderStatusRepository;

    public List<ShopOrderStatus> getAllShopOrderStatuses() {
        return shopOrderStatusRepository.findAll();
    }
}

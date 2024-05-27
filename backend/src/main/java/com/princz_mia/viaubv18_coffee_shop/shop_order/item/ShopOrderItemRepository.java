package com.princz_mia.viaubv18_coffee_shop.shop_order.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShopOrderItemRepository extends JpaRepository<ShopOrderItem, Long> {
    Optional<List<ShopOrderItem>> findAllByShopOrderId(Long shopOrderId);

    void deleteAllByShopOrderId(Long shopOrderId);
}

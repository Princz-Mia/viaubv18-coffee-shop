package com.princz_mia.viaubv18_coffee_shop.shop_order.status;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShopOrderStatusRepository extends JpaRepository<ShopOrderStatus, Long> {
    Optional<ShopOrderStatus> findByNameIgnoreCase(String name);
}

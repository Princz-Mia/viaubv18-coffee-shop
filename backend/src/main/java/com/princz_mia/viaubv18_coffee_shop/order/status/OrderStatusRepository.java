package com.princz_mia.viaubv18_coffee_shop.order.status;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderStatusRepository extends JpaRepository<OrderStatus, Long> {
    OrderStatus findByNameIgnoreCase(String name);
}

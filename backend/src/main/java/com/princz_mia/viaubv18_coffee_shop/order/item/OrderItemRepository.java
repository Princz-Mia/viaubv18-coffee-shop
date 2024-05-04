package com.princz_mia.viaubv18_coffee_shop.order.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    Optional<List<OrderItem>> findAllByOrderId(Long orderId);
}

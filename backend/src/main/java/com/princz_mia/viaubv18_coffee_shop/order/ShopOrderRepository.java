package com.princz_mia.viaubv18_coffee_shop.order;

import com.princz_mia.viaubv18_coffee_shop.address.Address;
import com.princz_mia.viaubv18_coffee_shop.order.status.OrderStatus;
import com.princz_mia.viaubv18_coffee_shop.shipping_method.ShippingMethod;
import com.princz_mia.viaubv18_coffee_shop.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShopOrderRepository extends JpaRepository<ShopOrder, Long> {
    Optional<List<ShopOrder>> findByCustomer(User user);
    Optional<List<ShopOrder>> findByShippingAddress(Address address);
    Optional<List<ShopOrder>> findByShippingMethod(ShippingMethod shippingMethod);
    Optional<List<ShopOrder>> findByOrderStatus(OrderStatus orderStatus);
    Optional<List<ShopOrder>> findByOrderDate(Timestamp orderDate);

    @Query("SELECT s FROM ShopOrder s WHERE s.customer.id = ?1 and LOWER(s.orderStatus.name) = LOWER(?2)")
    Optional<ShopOrder> findByCustomerIdAndOrderStatusIgnoreCase(Long userId, String name);
}

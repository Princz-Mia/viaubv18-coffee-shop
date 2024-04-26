package com.princz_mia.viaubv18_coffee_shop.shipping_method;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShippingMethodRepository extends JpaRepository<ShippingMethod, Long> {
    Optional<List<ShippingMethod>> findByPrice(Double price);
    Optional<ShippingMethod> findByName(String name);
}

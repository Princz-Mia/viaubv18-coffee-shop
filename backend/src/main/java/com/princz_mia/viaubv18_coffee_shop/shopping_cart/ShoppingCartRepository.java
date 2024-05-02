package com.princz_mia.viaubv18_coffee_shop.shopping_cart;

import com.princz_mia.viaubv18_coffee_shop.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    Optional<ShoppingCart> findByUser(User user);

    Optional<ShoppingCart> findByUserId(Long userId);
}

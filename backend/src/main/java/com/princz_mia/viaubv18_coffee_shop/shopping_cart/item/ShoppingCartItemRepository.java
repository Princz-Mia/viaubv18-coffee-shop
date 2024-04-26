package com.princz_mia.viaubv18_coffee_shop.shopping_cart.item;

import com.princz_mia.viaubv18_coffee_shop.shopping_cart.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShoppingCartItemRepository extends JpaRepository<ShoppingCartItem, Long> {
    Optional<List<ShoppingCartItem>> findByShoppingCart(ShoppingCart shoppingCart);
}

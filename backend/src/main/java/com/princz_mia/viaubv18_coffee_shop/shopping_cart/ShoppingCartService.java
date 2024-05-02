package com.princz_mia.viaubv18_coffee_shop.shopping_cart;

import com.princz_mia.viaubv18_coffee_shop.exception.AppException;
import com.princz_mia.viaubv18_coffee_shop.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;

    public void createShoppingCartForVerifiedUser(User user) {
        if (!user.isEnabled())
            throw new AppException("User is not verified yet. Could not create ShoppingCart", HttpStatus.BAD_REQUEST);

        ShoppingCart shoppingCart = new ShoppingCart(user);
        shoppingCartRepository.save(shoppingCart);
    }

    public ShoppingCart getShoppingCartById(Long id) {
        if (id == null || id < 0)
            throw new AppException("Cart Id is not valid", HttpStatus.BAD_REQUEST);

        var shoppingCart = shoppingCartRepository.findById(id);
        return shoppingCart.orElseThrow(() -> new AppException("Cart is not found with matching Cart Id", HttpStatus.NOT_FOUND));
    }

    public ShoppingCart getShoppingCartByUserId(Long userId) {
        if (userId == null || userId < 0)
            throw new AppException("User Id is not valid", HttpStatus.BAD_REQUEST);

        var shoppingCart = shoppingCartRepository.findByUserId(userId);
        return shoppingCart.orElseThrow(() -> new AppException("Cart is not found with matching User Id", HttpStatus.NOT_FOUND));
    }
}
